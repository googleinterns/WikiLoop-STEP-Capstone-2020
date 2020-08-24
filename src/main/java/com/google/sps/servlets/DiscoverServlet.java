package com.google.sps.servlets;
import java.lang.Math;
import java.lang.Double;
import java.io.IOException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
=======

import com.google.appengine.api.datastore.*;

>>>>>>> 3389198ffb6a91995f9bb219e1d1c83cfa2dfa39
import java.util.Date;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.ArrayList;
import java.util.StringTokenizer;
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

/** 
 * This Servlet goes to the datastore and retuns the list of Edit 
 */
@WebServlet("/comments")
public class DiscoverServlet extends HttpServlet {
  /* Given mock edit comments from Wikipedia, returns a list of Edit Comment Objects */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {                
    String ids = request.getParameter("ids");
    StringTokenizer st = new StringTokenizer(ids, "-");
    List<String> idList = new ArrayList<String>();
    while (st.hasMoreTokens()) {
     idList.add(st.nextToken());  
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    // Go through each comment and analyze comment's toxicity, creating an Edit Comment Object
    ArrayList editComments = new ArrayList<String>();
    if (ids.equals("null")) {
      Query query = new Query("EditComment");
      PreparedQuery results = datastore.prepare(query);
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
    } else {
      for (String id : idList) {
        Query query = new Query("EditComment").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
        PreparedQuery pq = datastore.prepare(query);
        Entity entity = pq.asSingleEntity();
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
