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

/** Servlet that returns UserProfile information */
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    
    // Get User's ID
    String id = getRequestParameters();
    // Get User's instance from Datastore
    User user = retrieveUser(id);

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
   * Return current User ID 
   */
  private String getRequestParameters () {

     ArrayList<String> ids = new ArrayList<String>();
     ids.add("100");
     ids.add("101");
     ids.add("102");

    int randomIndex = (int) (Math.random() * ids.size());

    String id =  ids.get(randomIndex);

    
    return id;
  }


  /** 
   * Retrieve a user from Datastore 
   */
   private User retrieveUser(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by userName
    Query query = 
        new Query("UserProfile")
            .setFilter(new Query.FilterPredicate("userName", Query.FilterOperator.EQUAL, "Tom"));
    PreparedQuery results = datastore.prepare(query);

    Entity entity = results.asSingleEntity();
    if (entity == null){
        return null;
    }
    String name = (String) entity.getProperty("userName");
    
    // Get User's edit comments list
    Collection<EmbeddedEntity> listEditCommentsEntity = (Collection<EmbeddedEntity>) entity.getProperty("listEditComments");
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    for (EmbeddedEntity embeddedEntity : listEditCommentsEntity) {

      String comment = (String) embeddedEntity.getProperty("comment");
      String date = (String) embeddedEntity.getProperty("date");
      String parentArticle = (String) embeddedEntity.getProperty("parentArticle");
      String status = (String) embeddedEntity.getProperty("status");
      String revisionID = (String) embeddedEntity.getProperty("revisionID");
      String toxicityObject = (String) embeddedEntity.getProperty("toxicityObject");

      EditComment ec = new EditComment(revisionID, name, comment, toxicityObject, date, parentArticle, status);
      listEditComments.add(ec);
    }

    User user = new User(id, name, listEditComments);

    return user;
  }
}