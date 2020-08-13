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

import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;

import com.google.gson.Gson;

import java.io.*;
import java.lang.Math;
import java.lang.Double;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;

/** Servlet that receives and store data from the Media API*/
@WebServlet("/load-data")
public class LoadDataServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
   // first check id number with data
   
    // this boolean variable determines whether we update the Datastore or not
    boolean updateDatastore = true;
    String json = getEditComments(request);
    // Get a collection of edit comments without a toxicity attribute using the MockData class
    Collection<EditComment> listMockComments = new ArrayList<EditComment>();
    listMockComments = new MockData(json).getMockComments();

    // Add toxicity attributes to the comments
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments = addToxicityBreakDown(listMockComments);

    List<String> ids = new ArrayList<String>();
    for (EditComment comment: listEditComments) {
      ids.add(comment.revisionId);
    }
    String urlID = "index.html?ids=";
    for (String id: ids) {
      urlID += id + "-";
    }
    // store the new data in Datastore
    if (updateDatastore){
      loadToDatastore(listEditComments); 
    }
    // Redirect back to the HTML page.
   response.sendRedirect(urlID);
   return;
  }

  /**
   * Goes through all edit comments passed though in a collection, and stores both the author and the edit comment.
   * Calls loadEditCommentToDatastore() and loadUserToDatastore()
   * @param Collection<EditComment> listEditComments
   */
  private void loadToDatastore(Collection<EditComment> listEditComments) {
    for (EditComment editComment : listEditComments) {
      loadEditCommentToDatastore(editComment);
      loadUserToDatastore(editComment);      
    }
  }

  /**
   * Stores the edit comments passed though in the Datastore if it does not already exist
   * @param EditComment editComment
   */
  private void loadEditCommentToDatastore(EditComment editComment) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("EditComment", editComment.getRevisionId() + "en");
    Query query = new Query("EditComment").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      Entity editCommentEntity = new Entity("EditComment", editComment.getRevisionId() + "en");
      editCommentEntity.setProperty("revisionId", editComment.getRevisionId());
      editCommentEntity.setProperty("userName", editComment.getUserName());
      editCommentEntity.setProperty("comment", editComment.getComment());
      editCommentEntity.setProperty("computedAttribute", editComment.getToxicityObject());
      editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
      editCommentEntity.setProperty("date", editComment.getDate());
      editCommentEntity.setProperty("status", editComment.getStatus());
      datastore.put(editCommentEntity);
    }
  }

  /**
   * Stores the author of the editComment in Datastore.
   * Adds the editComment's revision id to the list of revision ids of the author's editComments
   * @param EditComment editComment
   */
  private void loadUserToDatastore(EditComment editComment) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("UserProfile", "/wikipedia/en/User:" + editComment.getUserName());
    Query query = 
      new Query("UserProfile")
        .setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
            
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();

    // Get revision ids for the existing comments by the user
    Collection<String> listEditCommentsRevids = new ArrayList<String>();
    if (entity != null){
        listEditCommentsRevids = (Collection<String>) entity.getProperty("listEditComments");
        datastore.delete(entity.getKey());
    }
    if (!listEditCommentsRevids.contains(editComment.getRevisionId())){
        listEditCommentsRevids.add(editComment.getRevisionId());
    }

    Entity userProfileEntity = new Entity("UserProfile", "/wikipedia/en/User:" + editComment.getUserName());

    userProfileEntity.setProperty("userName", editComment.getUserName());
    userProfileEntity.setProperty("listEditComments", listEditCommentsRevids);
    datastore.put(userProfileEntity);
  }

  /**
   * Goes through all edit comments passed though in a collection without a toxicity attribute,
   * and add a toxicity attribute to each one by using the Perspective class
   * @param Collection<EditComment> listMockComments
   * @return Collection<EditComment> containing EditComments with toxicity attributes
   */
  private Collection<EditComment> addToxicityBreakDown(Collection<EditComment> listMockComments) {
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();

    for (EditComment comment : listMockComments){
      Attribute attribute = new Perspective(comment.comment, true).computedAttribute;
      Gson gson = new Gson();
      String attributeString = gson.toJson(attribute);
      comment.toxicityObject = attributeString;
      listEditComments.add(comment);
    }
    return listEditComments;
  }

  /**
   * Parses the HTTP request and return a string in a json format
   * @param HttpServletRequest request
   * @return String structured in a json format, containing comments from the Media API
   */
  private String getEditComments(HttpServletRequest request) {
    StringBuffer stringBuffer = new StringBuffer();
    String line = null;
    try {
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null){
            stringBuffer.append(line);
        } 
    } catch (Exception e) { System.out.println("EXCEPTION: \t" + e);}

    String json = stringBuffer.toString();
    return json;
  }
}