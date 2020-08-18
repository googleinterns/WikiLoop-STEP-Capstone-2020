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
import com.google.sps.data.WikiMedia;
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
@WebServlet("/commentsTest")
public class DiscoverServletTest extends HttpServlet {
  /* Given mock edit comments from Wikipedia, returns a list of Edit Comment Objects */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {                
    String ids = request.getParameter("ids");
    String prop = request.getParameter("prop"); 
    WikiMedia call = new WikiMedia();
    
    response.setContentType("application/json;"); 
    response.getWriter().println("a");
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
