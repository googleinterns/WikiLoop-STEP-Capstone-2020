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
import com.google.sps.data.MockComment;

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

/** Servlet that handles comments data */
/* TO DO (DEUS):   Use the actual EditComment Class as soon as David pushes some code*/
@WebServlet("/load-data")
public class LoadDataServlet extends HttpServlet {
  String USER_PROFILE_DATASTORE_ENTITY_NAME = "userProfileEntity";


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get request json
    StringBuffer stringBuffer = new StringBuffer();
    String line = null;
    try {
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null){
            stringBuffer.append(line);
        } 
    } catch (Exception e) { System.out.println("EXCEPTION: \t" + e);}

    String json = stringBuffer.toString();
    System.out.println(json);

    // TO DO (Maybe David): parse json to add EditComments and store them
    // parse json to mockdata
    // run through the perspective api
    // create edit comment
    // store edit comment
    List<MockComment> listMockComments = new ArrayList<MockComment>();
    listMockComments = new MockData(json).getMockComments();
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();

    for (MockComment comment : listMockComments){
      String toxicString = getToxicityString(comment.text);
      try { 
        JSONObject toxicityObject =(JSONObject) new JSONParser().parse(toxicString); 
        //System.out.println(toxicString); 
        // typecasting obj to JSONObject 
        JSONObject attributeScores = (JSONObject) toxicityObject.get("attributeScores");
        JSONObject toxicity = (JSONObject) attributeScores.get("TOXICITY");
        JSONObject summaryScore = (JSONObject) toxicity.get("summaryScore");
        String toxicScore = String.valueOf(Math.round((100 * Double.parseDouble(summaryScore.get("value").toString()))));
        EditComment analyzedComment = new EditComment(comment.revisionId, comment.userName, comment.text, 
                                              toxicScore, comment.date, comment.parentArticle, "NEW");
        listEditComments.add(analyzedComment);
      } catch (Exception e) {
        //System.out.println(e);
      }   
        
    }

    // TO DO (Deus): parse json and use David's work to add and edit UserProfiles and store them
    loadUserToDatastore(listEditComments);

    // Redirect back to the HTML page.
    response.sendRedirect("index.html");
  }

  private void loadUserToDatastore(Collection<EditComment> listEditComments) {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Entity userProfileEntity = new Entity("UserProfile", "Tom");
    userProfileEntity.setProperty("userName", "Tom");
    ArrayList<EmbeddedEntity> listEditCommentsEntity = new ArrayList<EmbeddedEntity>();
    for (EditComment ec : listEditComments) {
      System.out.println("USERNAME IS: \t" + ec.getUserName());
      EmbeddedEntity editCommentEntity = createEmbeddedEntity(ec);
      listEditCommentsEntity.add(editCommentEntity);
    }
    userProfileEntity.setProperty("listEditComments", listEditCommentsEntity);
    datastore.put(userProfileEntity);
  }

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
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   */
  private String getToxicityString(String comment) {
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      String api_key = "AIzaSyCLs-HQGTS_Fdpg3rvrbtb-XlOvsEgG3pQ";
      String buildUrl = ("https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze" +    
      "?key=" + api_key);
      String postUrl = buildUrl;
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(JSON, convertToJson(comment));
      Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
      Response response = client.newCall(request).execute();
      return response.body().string();
    }
    catch(IOException e) {
        e.printStackTrace();
    }
    return "Error";
  }
  
  /** 
   * Build json header for perspective api post request
   */
  private String convertToJson(String comment) {
    String json = "{";
    json += "\"comment\": ";
    json += "{\"text\": " + "\"" + comment + "\"" + "}";
    json += ", ";
    json += "\"languages\": ";
    json += "[\"en\"]";
    json += ", ";
    json += "\"requestedAttributes\": ";
    json += "{\"TOXICITY\": {}}";
    json += "}";
    return json;
  }

}