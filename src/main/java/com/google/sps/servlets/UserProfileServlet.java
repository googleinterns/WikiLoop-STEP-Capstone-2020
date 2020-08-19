// Copyright 2019 Google LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.sps.servlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.data.EditComment;

import com.google.gson.Gson;

import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

/** Servlet that returns UserProfile information */
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    
    // Get User's instance from Datastore
    List<String> listOfUsers = new ArrayList<String>();
    listOfUsers.add("Giano II");
    //listOfUsers.add("Bastun");
    Collections.shuffle(listOfUsers);
    String userToRetrieve = listOfUsers.get(0);
    User user = retrieveUser(userToRetrieve);

    // Jasonify the User (Convert the userprofile to JSON)
    Gson gson = new Gson();
    String json = gson.toJson(user);
    
    // Send the JSON as the response
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Redirect back to the HTML page.
    response.sendRedirect("index.html");
  }

  /** 
   * Retrieve a user with a given name from Datastore
   * @param String userToRetrieve
   * @return User
   */
  private User retrieveUser(String userToRetrieve) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("UserProfile", "/wikipedia/en/User:" + userToRetrieve);
    Query query = 
        new Query("UserProfile")
            .setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
            
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null){
        return null;
    }
    String id = String.valueOf(entity.getKey().getId());
    String userName = (String) entity.getProperty("userName");

    // Get User's edit comments revision ids
    Collection<String> listEditCommentsRevids = new ArrayList<String>();
    listEditCommentsRevids = (Collection<String>) entity.getProperty("listEditComments");
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments = retrieveUserEditComments(listEditCommentsRevids);
    User user = new User(id, userName, listEditComments);
    return user;
  }

  /**
   * Goes through the collection of revision ids and returns a collectino of EditComments retrieved from Datastore,
   * with the corresponding revision ids
   * @param Collection<String> listEditCommentsRevids
   * @return ArrayList<EditComment> of EditComments
   */
  private ArrayList<EditComment> retrieveUserEditComments(Collection<String> listEditCommentsRevids) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    for (String revid : listEditCommentsRevids) {
        // Filter query by the Key
        Key key = KeyFactory.createKey("EditComments", revid + "en");
        Query query = new Query("EditComments").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
        PreparedQuery results = datastore.prepare(query);
        Entity entity = results.asSingleEntity();

        String userName = (String) entity.getProperty("userName");
        String comment = (String) entity.getProperty("comment");
        String date = (String) entity.getProperty("date");
        String parentArticle = (String) entity.getProperty("parentArticle");
        String status = (String) entity.getProperty("status");
        String toxicityObject = (String) entity.getProperty("computedAttribute");
        String revisionId = (String) entity.getProperty("revisionId");

        try {
        JSONObject computedAttribute = (JSONObject) new JSONParser().parse(toxicityObject); 
        listEditComments.add(new EditComment(revisionId, userName, comment, computedAttribute.get("toxicityScore").toString(), date, parentArticle, status));

        } catch(Exception e) {
        System.out.println(e);
        }
    }
    return listEditComments;
  }
}