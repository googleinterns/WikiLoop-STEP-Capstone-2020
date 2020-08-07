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

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import com.google.gson.Gson;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;



/**
 * Class tests the DiscoverServlet logic using mock data from MockData.java
 *
 **Toxicity Scores can fluctuate, so if a test fails due to a change
 * in score, just update the score in the test file, mockDataTest.java
 */
@RunWith(JUnit4.class)
public class DiscoverServletTest {
  private DiscoverServletTest discover;
  private final JSONArray expectedJSON = new MockData().getExpectedResponse();
  
  /**
   * Reads the perspective api key in config json and initializes the
   * returns api_key in order to get toxicity score
   * @return API key string
   */
  private String getApiKey() {
   try { 
      Object obj = new JSONParser().parse(new FileReader("config.json")); 
      // typecasting obj to JSONObject 
      JSONObject jo = (JSONObject) obj;
      return (String) jo.get("WIKILOOP_API_KEY");
     } catch (Exception e) {
      return "";
     }  
  }

  /**
   * Mimics the doGet() function in DiscoverServlet.java function purpose is 
   * to get comments from mockData, emulating the WikiMedia API response, and
   * convert the mock comments into edit comment object, making call to perspective
   * api to get toxicity score. This function returns JSONArray in order to compare
   * with expected JSONArray result in Mock Data
   * @return JSONArray of EditComment Objects
   */
  public JSONArray doGet() throws IOException {
    List<EditComment> listMockComments = new ArrayList<EditComment>();
    listMockComments = new MockData().getMockComments();
   

    // Go through each comment and analyze comment's toxicity, creating an Edit Comment Object
    ArrayList editComments = new ArrayList<String>();
    for (EditComment mockComment : listMockComments) {
      
      String toxicString = getToxicityString(mockComment.comment);
      Perspective find  = new Perspective(mockComment.comment, false);
      try { 
        JSONObject toxicityObject = (JSONObject) new JSONParser().parse(toxicString); 
        
        // typecasting obj to JSONObject 
        JSONObject attributeScores = (JSONObject) toxicityObject.get("attributeScores");
        JSONObject toxicity = (JSONObject) attributeScores.get("TOXICITY");
        JSONObject summaryScore = (JSONObject) toxicity.get("summaryScore");
        String toxicScore = String.valueOf(Math.round((100 * Double.parseDouble(summaryScore.get("value").toString()))));
        mockComment.toxicityObject = toxicScore;
        editComments.add(mockComment);
      } catch (Exception e) {
        System.out.println(e);
      }   
    }

    // Convert List into JSONArray
    String json = convertToJsonUsingGson(editComments);
    JSONParser parser = new JSONParser();
    JSONArray array = (JSONArray) JSONValue.parse(json);
    return array;
  }

  /**
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   * @param  String comment text perspective api is analyzing
   * @return String JSON response from API
   */
  private String getToxicityString(String comment) {
    
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      String buildUrl = ("https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze" +    
      "?key=" + getApiKey());
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
   * Build JSON header for perspective api post request
   * @param String comment text that api will analyzed
   * @return JSON string of header for perspective api call
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
   * Converts a list of EditComment Objects into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   * @param List EditComment Objects
   * @return String JSONstring of EditComment Objects
   */
  private String convertToJsonUsingGson(List comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }

 /**
  * Initialize the DiscoverServlet Test
  */
 @Before
  public void setUp() throws IOException {
    discover = new DiscoverServletTest();
  }
  
 /**
  * Test to check if the doGet logic of Discover Servlet is working
  * reads mock data in mockData.json and compares the JSONarray output
  * to mockDataTest.json JSONArray of edit comments
  */
  @Test
  public void getsCorrectEditCommentsList() throws IOException {
    Assert.assertEquals(discover.doGet(), expectedJSON);
  }
}
