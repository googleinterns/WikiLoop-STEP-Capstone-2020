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
 * Class takes care of WikiMedia calls, allowing the user to state which 
 * properties they wish to get and state the ids for the place they want to
 * retrieve it from. For instance, getting revision summaries information from a
 * specific revision id
 */
public final class WikiMedia {
  public WikiMedia () {
  }

  /**
   * Given a list of strings this function creates a single string with
   * %7C or "|" as the delimited between them. This is used for params for 
   * WikiMedia API Calls
   * @param ids List of strings that need to be seperated by a string
   * @return formattedIds, strings that have been seperated
   */
   private String formatIds(List<String> ids) {
     String formattedIds = "";
     for (int i = 0; i < ids.size(); i++) {
       formattedIds += ids.get(i);
       if (i != ids.size() - 1) {
         formattedIds += "%7C";
       }
     }
     return formattedIds;
   }

  /**
   * Given revision id(s) this function return the WikiMedia response for finding
   * certain revisions by id.
   * @param ids List of revision ids 
   * @return WikiMedia API Response
   */
  public String getByRevIds(List<String> ids) {
    Map<String, String> params = ImmutableMap.<String, String>builder()
                                    .put("action", "query")
                                    .put("format", "json")
                                    .put("prop", "revisions")
                                    .put("revids", formatIds(ids))
                                    .build();
    return getWikiMediaResponse(params);
  }
  /**
   * Given a title and rvlimit, this function returns the revision summaries
   * of the title and  rvlimit amount of revisions from that title
   * @param titles title to call WikiMedia API
   * @param rvlimit The number of revisions to return for a title ** Only 1 - 5000 Revisions at a time**
   * @param rvstart Initialize time to find revisions
   * @param rvend Max time to find revisions
   * @param rvuser Specific user you want to look at
   * @return WikiMedia API Response
   */
  public String getByTitle(String title, String rvlimit, String rvstart, String rvend, String rvuser) {
    Map<String, String> params = ImmutableMap.<String, String>builder()
                                    .put("action", "query")
                                    .put("format", "json")
                                    .put("prop", "revisions")
                                    .put("titles", title)
                                    .put("rvlimit", rvlimit)
                                    .put("rvstart", rvstart)
                                    .put("rvend", rvend)
                                    .put("rvuser", rvuser)
                                    .build();
    return getWikiMediaResponse(params);
  }

  /**
   * Given a specific user name and a limit of the number of revisions to return
   * Function creates the params to call the WikiMedia api to get revision summaries
   * from specific user
   * @param user The username of the revision summaries to get
   * @param arvlimit The ammout of revisions to get for that user **Only 1 - 5000 Revisions at a time**
   */
  public String getByUser(String user, String arvlimit) {
    Map<String, String> params = ImmutableMap.<String, String>builder()
                                    .put("action", "query")
                                    .put("format", "json")
                                    .put("prop", "revisions")
                                    .put("list", "allrevisions")
                                    .put("arvuser", user)
                                    .put("arvlimit", arvlimit)
                                    .build();
    return getWikiMediaResponse(params);
  }

  /** 
   * Build JSON header for Wikimedia api request
   * @return JSON string of header for WikiMedia api call
   */
  private String getApiHeader () {
    String json = "{\"User-agent\": " + "\"WikiLoop DoubleCheck Team\"" + "}";
    return json;
  }

  /**
   * Function calls the perspective api via post request
   * to analyze an edit comment text
   * @param  comment comment text the perspective api is analyzing
   * @return String JSON response from API
   */
  private String getWikiMediaResponse(Map<String, String> params) {
    String buildUrl = "https://en.wikipedia.org/w/api.php"; 
    buildUrl = buildUrl + "?origin=*";
    for (Map.Entry<String,String> entry : params.entrySet()) {
      buildUrl += "&" + entry.getKey() + "=" + entry.getValue();
    }  
    try {
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      String postUrl = buildUrl;
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(JSON, getApiHeader());
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
