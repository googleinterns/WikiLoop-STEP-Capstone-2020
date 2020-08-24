package com.google.sps.data;
import java.util.Date;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import com.google.sps.data.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.lang.Double;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Call;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.Collections;
import java.io.FileReader;


/** 
 * Takes the response of the Perspective API to store and determine the
 * classifcation of the text on different attributes of TOXICITY, IDENTITY_ATTACK,
 * INSULT, PROFANITY, THREAT, SEXUALLY_EXPLICIT, FLIRTATION.
 * All attributes except for TOXICITY are experimental, thus will make mistakes and not 
 * tested for issues including user bias. When experimental attributes go into production, the code must be changed
 * Refer to this doc to learn more https://github.com/conversationai/perspectiveapi/blob/master/2-api/models.md
 */
public final class Perspective {
  private JSONObject perspectiveResponse;
  private List<String> labels = Arrays.asList("TOXICITY", "IDENTITY_ATTACK", "INSULT", "PROFANITY", "THREAT", "SEXUALLY_EXPLICIT", "FLIRTATION");
  private List<String> expLabels = Arrays.asList("IDENTITY_ATTACK", "INSULT", "PROFANITY", "THREAT", "SEXUALLY_EXPLICIT", "FLIRTATION");
  private HashSet<String> checkExpLabels = new HashSet<String>(expLabels);
  private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
  private String TOXIC_REASON = "Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion";
  private String INDENTITY_REASON = "Negative or hateful comments targeting someone because of their identity";
  private String PROFANITY_REASON = "Swear words, curse words, or other obscene or profane language";
  private String THREAT_REASON = "Describes an intention to inflict pain, injury, or violence against an individual or group";
  private String SEXUALLY_EXPLICIT_REASON = "Contains references to sexual acts, body parts, or other lewd content";
  private String FLIRTATION_REASON = "Pickup lines, complimenting appearance, subtle sexual innuendos, etc";
  private Map<String, String> reason = ImmutableMap.<String, String>builder()
                                    .put("TOXICITY", TOXIC_REASON)
                                    .put("IDENTITY_ATTACK", INDENTITY_REASON)
                                    .put("PROFANITY", PROFANITY_REASON)
                                    .put("THREAT",THREAT_REASON)
                                    .put("SEXUALLY_EXPLICIT", SEXUALLY_EXPLICIT_REASON)
                                    .put("FLIRTATION", FLIRTATION_REASON)
                                    .build();
  public Attribute computedAttribute;
  /**
   * Takes in a comment and desire to consider expermental attributes.
   * if experimental is true then we want to consider experimental attributes
   * Requests perspective api to get attribute label and attribute score. 
   * returns the attribute with the highest probability and percentage
   * @param comment, boolean Experminental
   * @return Attribute containing label, score, reason, and if attribute is experimental
   */
  public Perspective(String comment, boolean experimental) {
    try { 
      this.perspectiveResponse = (JSONObject) new JSONParser().parse(getToxicityString(comment));
      //System.out.println(this.perspectiveResponse);
      setListAttributes(experimental);
      Collections.sort(attributes, Attribute.ORDER_BY_HIGH);
      this.computedAttribute = attributes.get(0);
    } catch (Exception e) {
      System.out.println(e.toString());
    }   
  }
  
  /**
   * Goes through all the labels of the known perspective api attributes to
   * determines if we should add the label
   * @param experimental, true if we to consider experimental labels, false if not 
   */
  private void setListAttributes(boolean experimental) {
    for (String label: labels) {
      if (experimental) {
        attributes.add(setAttribute(label, experimental));
      } else if (!checkExpLabels.contains(label)) {
        attributes.add(setAttribute(label, experimental));
      }
    }
  }

  /**
   * Takes in a certain label and classification if label is experimental
   * parses the toxicity object to find summary score and creates an
   * Attribute object scoring label, score, label explaination, and if label is experimental
   * @param experimental, true if we to consider experimental labels, false if not
   * @return Attribute object
   */
  private Attribute setAttribute(String label, boolean experimental) {
    JSONObject attributeScores = (JSONObject) perspectiveResponse.get("attributeScores");
    JSONObject labelAttribute = (JSONObject) attributeScores.get(label);
    JSONObject summaryScore = (JSONObject) labelAttribute.get("summaryScore");
    String score = String.valueOf(Math.round((100 * Double.parseDouble(summaryScore.get("value").toString()))));
    return new Attribute(label, score, reason.get(label), checkExpLabels.contains(label));
  }

  /** 
   * Build JSON header for perspective api post request
   * @param comment text that api will analyzed
   * @return JSON string of header for perspective api call
   */
  private String getApiHeader (String comment) {
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
      return "AIzaSyCLs-HQGTS_Fdpg3rvrbtb-XlOvsEgG3pQ";
     }  
  }

  /**
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   * @param  comment comment text the perspective api is analyzing
   * @return String JSON response from API
   */
  private String getToxicityString(String comment) {
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      //System.out.println(getApiKey());
      String buildUrl = ("https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze" +    
      "?key=" + getApiKey());
      String postUrl = buildUrl;
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(JSON, getApiHeader(comment));
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
}