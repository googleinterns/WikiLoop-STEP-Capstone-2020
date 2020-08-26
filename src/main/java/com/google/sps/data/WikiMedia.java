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
  public String getByRecentChanges() {
    Map<String, String> params = ImmutableMap.<String, String>builder()
                                    .put("action", "query")
                                    .put("format", "json")
                                    .put("list","recentchanges")
                                    //.put("rcnamespace","1|2|3|4|5|11|9|7|12|13|15|101|109")
                                    .put("rcnamespace","2%7C3%7C4%7C5%7C7%7C9%7C11%7C13%7C15%7C101%7C109%7C119%7C447%7C711%7C829%7C2301%7C2302")
                                    .put("rcprop","title%7Ctimestamp%7Cids%7Ccomment%7Cuser%7Cuserid")
                                    .put("rclimit","max")
                                    .build();
    return getWikiMediaResponse(params);
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
                .header("User-agent", "WikiLoop DoubleCheck Team")
                .post(body)
                .build();
      Response response = client.newCall(request).execute();
      return response.body().string();
    }
    catch(IOException e) {
        System.out.println(e);
        e.printStackTrace();
    }
    return "Error";
  }

  /**
   * Function gets a response from the MediaAPI in a string json format,
   * parses the string, and returns a list of EditComments
   * @param  String json response from the WikiMedia API
   * @return List<EditComment> A list of EditComments 
   */
   public List<EditComment> readWikiMediaResponse(String json) {
    ArrayList<EditComment> editComments = new ArrayList<EditComment>();

    /* Reads JSON file & converts to edit comment */
    try { 
      Object jsonObj = new JSONParser().parse(json); 

      /* Parse mockDataJSON */
      JSONObject mockDataObject = (JSONObject) jsonObj;
      JSONObject query = (JSONObject) mockDataObject.get("query");

      for (Object key : query.keySet()) {
        String queryType = String.valueOf(key);
        if (queryType.equals("pages")) {
          editComments.addAll(readByRevisionID((JSONObject) query.get(queryType)));
        }
        else if (queryType.equals("allrevisions")) {
          editComments.addAll(readByAllRevisions((JSONArray) query.get(queryType)));
        }
        else if (queryType.equals("recentchanges")) {
          editComments.addAll(readByRecentChanges((JSONArray) query.get(queryType)));
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    
    return editComments;
  }

  /**
   * Function is a helper function for the readWikiMediaResponse function,.
   * parses a JSONObject and returns a list of EditComments.
   * @param  JSONObject pages
   * @return List<EditComment> A list of EditComments 
   */
  private List<EditComment> readByRevisionID(JSONObject pages) {
    ArrayList<EditComment> editComments = new ArrayList<EditComment>();

    /* Reads JSON file & converts to edit comment */
    try { 
      for (Object key : pages.keySet()) {
          String pageId = String.valueOf(key);
          JSONObject jsonKey = (JSONObject) pages.get(pageId);
          String article = (String) jsonKey.get("title");
          JSONArray revisions = (JSONArray) jsonKey.get("revisions");
          
        for (Object o: revisions) {
          JSONObject comment = (JSONObject) o;
          String revisionId = String.valueOf(comment.get("revid"));
          String user = (String) comment.get("user");
          String mockComment = (String) comment.get("comment");
          String date = (String) comment.get("timestamp");
          editComments.add(new EditComment(revisionId, user, mockComment.replaceAll("/\\*.*\\*/", ""), "0", date, article, "NEW", "0", "0", "0"));
        //editComments.add(new EditComment(revisionId, user, (mockComment.replaceAll("Undid revision (\\d)* by \\[\\[(.)*\\]\\] \\(\\[\\[(.)*\\]\\]\\)", "")).replaceAll("(\\/\\)(.)(\\*\\/)", ""), "0", date, article, "NEW", "0", "0", "0"));
       }
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return editComments;
  }

  /**
   * Function is a helper function for the readWikiMediaResponse function,.
   * parses a JSONArray and returns a list of EditComments.
   * @param  JSONArray allrevisions
   * @return List<EditComment> A list of EditComments 
   */
  private List<EditComment> readByAllRevisions(JSONArray allrevisions) {
    ArrayList<EditComment> editComments = new ArrayList<EditComment>();

    /* Reads JSON file & converts to edit comment */
    try { 
      for (Object obj : allrevisions) {
          JSONObject object = (JSONObject) obj;
          String pageId = String.valueOf(object.get("pageid"));
          String article = (String) object.get("title");
          JSONArray revisions = (JSONArray) object.get("revisions");
          
          for (Object o: revisions) {
            JSONObject comment = (JSONObject) o;
            String revisionId = String.valueOf(comment.get("revid"));
            String user = (String) comment.get("user");
            String mockComment = (String) comment.get("comment");
            String date = (String) comment.get("timestamp");
            editComments.add(new EditComment(revisionId, user, mockComment.replaceAll("/\\*.*\\*/", ""), "0", date, article, "NEW", "0", "0", "0"));
        //editComments.add(new EditComment(revisionId, user, (mockComment.replaceAll("Undid revision (\\d)* by \\[\\[(.)*\\]\\] \\(\\[\\[(.)*\\]\\]\\)", "")).replaceAll("(\\/\\)(.)(\\*\\/)", ""), "0", date, article, "NEW", "0", "0", "0"));
          }
       }

    } catch (Exception e) {
      System.out.println(e);
    }
    return editComments;
  }

  /**
   * Function is a helper function for the readWikiMediaResponse function,.
   * parses a JSONArray and returns a list of EditComments.
   * @param  JSONArray recentchanges
   * @return List<EditComment> A list of EditComments 
   */
   private List<EditComment> readByRecentChanges(JSONArray recentchanges) {
    ArrayList<EditComment> editComments = new ArrayList<EditComment>();

    /* Reads JSON file & converts to edit comment */
    try { 
      for (Object obj : recentchanges) {
          JSONObject object = (JSONObject) obj;
          String pageId = String.valueOf(object.get("pageid"));
          String article = (String) object.get("title");
          
          String revisionId = String.valueOf(object.get("revid"));
          String user = (String) object.get("user");
          String mockComment = (String) object.get("comment");
          String date = (String) object.get("timestamp");
          editComments.add(new EditComment(revisionId, user, mockComment.replaceAll("/\\*.*\\*/", ""), "0", date, article, "NEW", "0", "0", "0"));
        //editComments.add(new EditComment(revisionId, user, (mockComment.replaceAll("Undid revision (\\d)* by \\[\\[(.)*\\]\\] \\(\\[\\[(.)*\\]\\]\\)", "")).replaceAll("(\\/\\)(.)(\\*\\/)", ""), "0", date, article, "NEW", "0", "0", "0"));
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return editComments;
  }
}
