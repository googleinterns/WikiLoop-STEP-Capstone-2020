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
  
   
    
    // this boolean variable determines whether we update the Datastore or not
    boolean updateDatastore = true;
    String json = getEditComments(request);
    System.out.println(json);
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
      loadEditCommentsToDatastore(listEditComments);    
      loadUsersToDatastore(listEditComments); 
    }
    // Redirect back to the HTML page.
   response.sendRedirect(urlID);
   return;
    
  }

  /**
   * Goes through all edit comments passed though in a collection, and stores them to the Datastore
   * @param Collection<EditComment> listEditComments
   */
  private void loadEditCommentsToDatastore(Collection<EditComment> listEditComments) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      for (EditComment editComment : listEditComments) {
        Query query = new Query("EditComments").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, editComment.revisionId));
        PreparedQuery pq = datastore.prepare(query);
        Entity entity = pq.asSingleEntity();
        if (entity == null) {
          Entity editCommentEntity = new Entity("EditComments", editComment.getRevisionId() + "en");
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
  }

  /**
   * Goes through all edit comments passed though in a collection, and stores the author in Datastore.
   * Stores a list of EditComments for each author.
   * Updates the list if an existing author has a new comment
   * @param Collection<EditComment> listEditComments
   */
  private void loadUsersToDatastore(Collection<EditComment> listEditComments) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    for (EditComment editComment : listEditComments) {
      Query query = 
        new Query("UserProfile")
            .setFilter(new Query.FilterPredicate("userName", Query.FilterOperator.EQUAL, editComment.getUserName()));
      PreparedQuery results = datastore.prepare(query);
      Entity entity = results.asSingleEntity();
      if (entity == null) {
      Collection<EmbeddedEntity> listEditCommentsEntity = new ArrayList<EmbeddedEntity>();
      if (entity != null){
        listEditCommentsEntity = (Collection<EmbeddedEntity>) entity.getProperty("listEditComments");
        datastore.delete(entity.getKey());
      }
      
      EmbeddedEntity editCommentEntity = createEmbeddedEntity(editComment);
      if (!containsEditComment(listEditCommentsEntity, editComment)){
          listEditCommentsEntity.add(editCommentEntity);
          }

      Entity userProfileEntity = new Entity("UserProfile", editComment.getUserName());
      userProfileEntity.setProperty("userName", editComment.getUserName());
      userProfileEntity.setProperty("listEditComments", listEditCommentsEntity);
      datastore.put(userProfileEntity);
      }
    }
  }

  /**
   * Sets properties for an EditComment EmbeddedEntity
   * Returns the EmbeddedEntity to be added to Datastore as a user's property
   * @param EditComment editComment
   * @return EmbeddedEntity describing the EditComment
   */
  private EmbeddedEntity createEmbeddedEntity(EditComment editComment) {
    EmbeddedEntity editCommentEntity = new EmbeddedEntity();
    editCommentEntity.setProperty("userName", editComment.getUserName());
    editCommentEntity.setProperty("comment", editComment.getComment());
    editCommentEntity.setProperty("date", editComment.getDate());
    editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
    editCommentEntity.setProperty("status", editComment.getStatus());
    editCommentEntity.setProperty("revisionID", editComment.getRevisionId());
    editCommentEntity.setProperty("toxicityObject", editComment.getToxicityObject());
    return editCommentEntity;
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
      //System.out.println("Comment: " + comment.comment + " " + attributeString);
      comment.toxicityObject = attributeString;
      listEditComments.add(comment);
    }
    return listEditComments;
  }

  /**
   * Goes through all EditComment entities passed though a collection, and checks if any of them 
   * contains an EditComment that equals the second EditComment (second parameter)
   * @param Collection<EditComment> listEditCommentsEntity
   * @param EditComment editComment
   * @return boolean
   */
  private boolean containsEditComment(Collection<EmbeddedEntity> listEditCommentsEntity, EditComment editComment){
      for (EmbeddedEntity embeddedEntity : listEditCommentsEntity){
        String userName = (String) embeddedEntity.getProperty("userName");
        String comment = (String) embeddedEntity.getProperty("comment");
        String date = (String) embeddedEntity.getProperty("date");
        String parentArticle = (String) embeddedEntity.getProperty("parentArticle");
        String status = (String) embeddedEntity.getProperty("status");
        String revisionId = (String) embeddedEntity.getProperty("revisionID");
        String toxicityObject = (String) embeddedEntity.getProperty("toxicityObject");
        EditComment tempEditComment = new EditComment(revisionId, userName, comment, toxicityObject, date, parentArticle, status);

        if (editComment.equals(tempEditComment)){
            return true;
        }
      }
      return false;
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