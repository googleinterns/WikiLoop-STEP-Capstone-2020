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
import com.google.sps.data.Users;
import com.google.sps.data.EditComment;
import com.google.gson.Gson;
import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
/* TO DO (DEUS):   Use the actual EditComment Class as soon as David pushes some code*/
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  String DATASTORE_ENTITY_NAME = "userEntity";
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

    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","66.55", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","83.79", "September, 6 2019, 21:09","incivility", "pending");
    listEditComments.add(ec1);
    listEditComments.add(ec2);

    User user = new User(id, "Tom", listEditComments);
     /*Users users = new Users();
     
     for(int i = 0; i < users.users.size(); i++){
         if (users.users.get(i).id == id){
           return users.users.get(i);
         }
         
     }
     System.out.println(users.users.get(i).avgToxicityScore);

     String userName = "Tom";
     //long id = 1234;
     String avgToxicityScore = "47.35%";

     User user = new User(id, userName, avgToxicityScore, list_edit_comments); */
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