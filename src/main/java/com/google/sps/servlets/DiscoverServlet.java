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
import java.lang.Math;
import java.lang.Double;
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
import java.lang.Double;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;

/** Servlet that returns a list of Edit Comment Objects */
@WebServlet("/comments")
public class DiscoverServlet extends HttpServlet {
  /* Given mock edit comments from Wikipedia, returns a list of Edit Comment Objects */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {                
    Query query = new Query("EditComments");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Go through each comment and analyze comment's toxicity, creating an Edit Comment Object
    ArrayList editComments = new ArrayList<String>();
   for (Entity entity : results.asIterable()) {
      String revisionId = (String) entity.getProperty("revisionId");
      String user = (String) entity.getProperty("userName");
      String comment = (String) entity.getProperty("comment");
      String computedAttributeString = (String) entity.getProperty("computedAttribute");
      String article = (String) entity.getProperty("parentArticle");
      String date = (String) entity.getProperty("date");
      String status = (String) entity.getProperty("status");
      try {
        JSONObject computedAttribute = (JSONObject) new JSONParser().parse(computedAttributeString); 
        editComments.add(new EditComment(revisionId, user, comment, computedAttribute.get("score").toString(), date, article, status));
      } catch(Exception e) {
        System.out.println(e);
      }
    }
    String json = convertToJsonUsingGson(editComments);
    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
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

  /**
   * Converts a comments instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertAttributeToString(Attribute label) {
    Gson gson = new Gson();
    String json = gson.toJson(label);
    return json;
  }
}

