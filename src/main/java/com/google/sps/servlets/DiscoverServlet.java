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

import java.io.IOException;

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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.Date;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.MockComment;

/** Servlet that returns a list of Edit Comment Objects */
@WebServlet("/comments")
public class DiscoverServlet extends HttpServlet {
  /* Given mock edit comments from Wikipedia, returns a list of Edit Comment Objects */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<MockComment> listMockComments = new ArrayList<MockComment>();
    listMockComments = new MockData().getMockComments();
                
    Query query = new Query("edit-comments");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Go through each comment and analyze comment's toxicity, creating an Edit Comment Object
    ArrayList editComments = new ArrayList<String>();
    for (MockComment comment : listMockComments) {
      String toxicString = getToxicityString(comment.text);
      EditComment analyzedComment = new EditComment(comment.revisionId, comment.userName, comment.text, 
                                              toxicString, comment.date, comment.parentArticle, "NEW");
      editComments.add(analyzedComment);
    }
    String json = convertToJsonUsingGson(editComments);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   */
  private String getToxicityString(String comment) {
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      String api_key = "";
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

  /**
   * Converts a comments instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(List comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}

