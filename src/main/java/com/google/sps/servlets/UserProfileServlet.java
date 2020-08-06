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

import java.lang.reflect.Type; 
import com.google.gson.reflect.TypeToken;  

/** Servlet that handles comments data */
/* TO DO (DEUS):   Use the actual EditComment Class as soon as David pushes some code*/
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  String USER_PROFILE_DATASTORE_ENTITY_NAME = "userProfileEntity";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    
    // Get User's ID
    long id = getRequestParameters();
    // Get User's instance from Datastore
    User user = retrieveUser(id);

    // Jasonify the User (Convert the server stats to JSON)
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

  /* Get the request values */
  /* TO DO (DEUS): Connect to the Discover page */
  private long getRequestParameters () {
      // Get the id from the request
      // Use the Authentication API to see who is logged in

      // For now get an id randomly and return it
     

     ArrayList<Integer> ids = new ArrayList<Integer>();
     ids.add(100);
     ids.add(101);
     ids.add(102);

    int randomIndex = (int) (Math.random() * ids.size());

    int id =  ids.get(randomIndex);

    
    return (long) 100;
  }


  /** Retrieves a user from Datastore */
  /* TO DO (DEUS):   Get data from Datastore instead of an array list*/
   private User retrieveUser(long id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = 
        new Query("UserProfile")
            .setFilter(new Query.FilterPredicate("userName", Query.FilterOperator.EQUAL, "Tom"));
    PreparedQuery results = datastore.prepare(query);

    Entity entity = results.asSingleEntity();
    if (entity == null){
        return null;
    }
    String name = (String) entity.getProperty("userName");
    

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