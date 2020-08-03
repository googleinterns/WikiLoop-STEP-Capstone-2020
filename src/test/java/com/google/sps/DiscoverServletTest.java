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

package com.google.sps;
import java.lang.Math;
import java.lang.Double;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Call;

import java.io.FileReader; 

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;  
import org.json.simple.JSONValue;
import org.json.simple.parser.*; 

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import com.google.gson.Gson;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.MockComment;


/**
 * Class tests the DiscoverServlet logic using mock data from MockData.java
 */
@RunWith(JUnit4.class)
public class DiscoverServletTest {
  /* Given mock edit comments from Wikipedia, returns a list of Edit Comment Objects */
  
  private DiscoverServletTest discover;
  private final JSONArray expectedJSON = new MockData().getExpectedResponse();
  private String api_key;

  private void setApiKey() {
   try { 
      Object obj = new JSONParser().parse(new FileReader("config.json")); 
      // typecasting obj to JSONObject 
      JSONObject jo = (JSONObject) obj;
      api_key = (String) jo.get("WIKILOOP_API_KEY");
     } catch (Exception e) {
      api_key = "";
     }  
  }

  public JSONArray doGet() throws IOException {
    List<MockComment> listMockComments = new ArrayList<MockComment>();
    listMockComments = new MockData().getMockComments();
    // Go through each comment and analyze comment's toxicity, creating an Edit Comment Object
    ArrayList editComments = new ArrayList<String>();
    for (MockComment comment : listMockComments) {
      String toxicString = getToxicityString(comment.text);

      try { 
        JSONObject toxicityObject =(JSONObject) new JSONParser().parse(toxicString); 
        System.out.println(toxicString); 
        // typecasting obj to JSONObject 
        JSONObject attributeScores = (JSONObject) toxicityObject.get("attributeScores");
        JSONObject toxicity = (JSONObject) attributeScores.get("TOXICITY");
        JSONObject summaryScore = (JSONObject) toxicity.get("summaryScore");
        String toxicScore = String.valueOf(Math.round((100 * Double.parseDouble(summaryScore.get("value").toString()))));
        EditComment analyzedComment = new EditComment(comment.revisionId, comment.userName, comment.text, 
                                              toxicScore, comment.date, comment.parentArticle, "NEW");
        editComments.add(analyzedComment);
      } catch (Exception e) {
        System.out.println(e);
      }   
    }
    String json = convertToJsonUsingGson(editComments);
    JSONParser parser = new JSONParser();
    JSONArray array = (JSONArray) JSONValue.parse(json);
    System.out.println(array);
    return array;
  }

  /**
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   */
  private String getToxicityString(String comment) {
    setApiKey();
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
    json += "{\"TOXICITY\": {}, \"IDENTITY_ATTACK\": {}, \"INSULT\": {}, \"PROFANITY\": {}, \"THREAT\": {}, \"SEXUALLY_EXPLICIT\": {}, \"FLIRTATION\": {}}";
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

 @Before
  public void setUp() throws IOException {
    discover = new DiscoverServletTest();
  }

  @Test
  public void getsCorrectEditCommentsList() throws IOException {
    Assert.assertEquals(discover.doGet(), expectedJSON);
  }
}
