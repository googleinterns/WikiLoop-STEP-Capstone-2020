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

import com.google.appengine.api.datastore.*;

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
 * Servlet is responsible for sending a queried list of edit comments
 * from the datastore to the front end of the website, the ids to return
 * are passed through the url as parameters
 */
@WebServlet("/comments")
public class DiscoverServlet extends HttpServlet {
  /**
   * Get the comments in the datastore that match the id pass through
   * When no ids are given, doGet returns all edit comments in the database to review
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("EditComments");
    PreparedQuery results = datastore.prepare(query);              
    String ids = request.getParameter("id");

    ArrayList editComments = new ArrayList<EditComment>();

    // Check if any ids were passed through, if not return all edit comments in datastore
    if (ids == null || ids.equals("") || ids.equals("null")) {
      loadAllRevisions(editComments, results, datastore);
    } else {
      List<String> idList = createListIds(ids);
      loadSpecificRevisions(idList, datastore, editComments);
    }

    response.setContentType("application/json;"); 
    response.getWriter().println(convertToJsonUsingGson(editComments));
  }

  /**
   * Adds all the revisions in datastore into an array of edit comments to be send to front end
   * @param editComments Arraylist of edit comments
   * @param results PreparedQuery of revision summaries in datastore
   */
    private void loadAllRevisions(ArrayList<EditComment> editComments, PreparedQuery results, DatastoreService datastore) {
    //listEditComments.add(new EditComment())
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
        editComments.add(new EditComment(revisionId, user, comment, computedAttribute.toString(), date, article, status));
        Entity editCommentEntity = new Entity("TestEditComments", revisionId + "en");
        editCommentEntity.setProperty("revisionId", revisionId);
        editCommentEntity.setProperty("userName", user);
        editCommentEntity.setProperty("comment", comment);
        editCommentEntity.setProperty("computedAttribute", computedAttributeString);
        editCommentEntity.setProperty("parentArticle", article);
        editCommentEntity.setProperty("date", date);
        editCommentEntity.setProperty("status", status);
        datastore.put(editCommentEntity);
      } catch(Exception e) {
        System.out.println(e);
      }
    }
  }

  /**
   * Takes in a string which has a list of ids seperated with
   * - lines and adds each individual string into a list
   * @param ids String of multiple ids
   * @return List of individual ids
   */
  private List<String> createListIds(String ids) {
    StringTokenizer st = new StringTokenizer(ids, "-");
    List<String> idList = new ArrayList<String>();
    while (st.hasMoreTokens()) {
     idList.add(st.nextToken());  
    }
    return idList;
  }


  /**
   * Given a list of specific ids, retrieve ids from the database
   * @param idList List of ids to find in database
   * @param datastore Location of database
   */ 
  private void loadSpecificRevisions(List<String> idList, DatastoreService datastore, ArrayList<EditComment> editComments) {
    for (String id : idList) {
        Query query = new Query("EditComments").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
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
          editComments.add(new EditComment(revisionId, user, comment, computedAttribute.toString(), date, article, status));
        } catch(Exception e) {
          System.out.println(e);
        }
      }
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