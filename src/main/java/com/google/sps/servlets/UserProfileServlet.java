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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  String DATASTORE_ENTITY_NAME = "userEntity";
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    
    // Get User's ID

    // Get User's instance from Datastore
    User user = retrieveUser();

    // Jasonify the User (Convert the server stats to JSON)
    Gson gson = new Gson();
    String json = gson.toJson(user);
    
    // Send the JSON as the response
    System.out.println(json);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Redirect back to the HTML page.
    response.sendRedirect("index.html");
  }

  /** Returns the choice entered by the user, or 5 if the choice was invalid. 
  private int getUserChoice(HttpServletRequest request) {
    // Get the input from the form.

    return userChoice;
  } */

  /** Retrieves a user from Datastore */
   private User retrieveUser() {
     ArrayList<String> list_edit_comments = new ArrayList<String>();
     list_edit_comments.add("Test Comment #1: Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
     list_edit_comments.add("Test Comment #2: Adipisci amet autem commodi, consequatur debitis.");
     list_edit_comments.add("Test Comment #3: Doloremque esse fuga, iure laudantium magnam.");
     for(int i = 0; i < list_edit_comments.size(); i++)System.out.println(list_edit_comments.get(i));

     String userName = "Tom";
     long id = 1234;
     String avgToxicityScore = "47.35%";

     User user = new User(id, userName, avgToxicityScore, list_edit_comments);
    /*Query query = new Query(DATASTORE_ENTITY_NAME).addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    Entity entity = results.asIterable()[0];

      long id = entity.getKey().getId();
      String userName = (String) entity.getProperty("userName");
      String avgToxicityScore = (String) entity.getProperty("avgToxicityScore");
      ArrayList<String> list_edit_comments =(ArrayList<String>) entity.getProperty("list_edit_comments");

      
    User user = new User(id, userName, avgToxicityScore, list_edit_comments); */

    return user;
  }
}