package com.google.sps.servlets;
import java.lang.Math;
import java.lang.Double;
import java.io.IOException;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.util.Collection;
import com.google.appengine.api.datastore.*;

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
import com.google.sps.data.WikiMedia;
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
    Query query = new Query("EditComment");
    PreparedQuery results = datastore.prepare(query);              
    String ids = request.getParameter("id");
    String type = request.getParameter("type");
    String num = request.getParameter("num");
    if (num == null || num.equals("") || num.equals("null")) {
      num = "1";
    }
    ArrayList editComments = new ArrayList<EditComment>();
    // Check if any ids were passed through, if not return all edit comments in datastore
    if (ids == null || ids.equals("") || ids.equals("null")) {
      loadAllRevisions(editComments, results, datastore);
    } else {
      System.out.println(type);
      List<String> idList = createListIds(ids);
      /* Get specific ids for the type of query */
      if (type.equals("user")) {
        idList = getUserIds(idList, num);
      }
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
    for (Entity entity : results.asIterable()) {  
      String revisionId = (String) entity.getProperty("revisionId");
      String user = (String) entity.getProperty("userName");
      String comment = (String) entity.getProperty("comment");
      String computedAttributeString = (String) entity.getProperty("computedAttribute");
      String article = (String) entity.getProperty("parentArticle");
      String date = (String) entity.getProperty("date");
      String status = (String) entity.getProperty("status");
      String looksGoodCounter = (String) entity.getProperty("looksGoodCounter");
      String shouldReportCounter = (String) entity.getProperty("shouldReportCounter");
      String notSureCounter = (String) entity.getProperty("notSureCounter");

      try {
        JSONObject computedAttribute = (JSONObject) new JSONParser().parse(computedAttributeString); 
        Entity editCommentEntity = new Entity("TestEditComments", revisionId + "en");
        editCommentEntity.setProperty("revisionId", revisionId);
        editCommentEntity.setProperty("userName", user);
        editCommentEntity.setProperty("comment", comment);
        editCommentEntity.setProperty("computedAttribute", computedAttributeString);
        editCommentEntity.setProperty("parentArticle", article);
        editCommentEntity.setProperty("date", date);
        editCommentEntity.setProperty("status", status);
        datastore.put(editCommentEntity);

        editComments.add(new EditComment(revisionId, user, comment, computedAttribute.toString(), date, article, status, looksGoodCounter, shouldReportCounter, notSureCounter));
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
        Query query = new Query("EditComment").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
        PreparedQuery pq = datastore.prepare(query);
        Entity entity = pq.asSingleEntity();
<<<<<<< HEAD
        if (entity == null) {
          addIdToDatastore(id, datastore);
          query = new Query("EditComment").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
          pq = datastore.prepare(query);
          entity = pq.asSingleEntity();
        }
        // If comment still isn't added to database don't load
        if (entity != null) {
          String revisionId = (String) entity.getProperty("revisionId");
          String user = (String) entity.getProperty("userName");
          String comment = (String) entity.getProperty("comment");
          String computedAttributeString = (String) entity.getProperty("computedAttribute");
          String article = (String) entity.getProperty("parentArticle");
          String date = (String) entity.getProperty("date");
          String status = (String) entity.getProperty("status");
          try {
            JSONObject computedAttribute = (JSONObject) new JSONParser().parse(computedAttributeString); 
            editComments.add(new EditComment(revisionId, user, comment, computedAttribute.toString(), date, article, status, "", "", ""));
          } catch(Exception e) {
            System.out.println(entity);
          }
=======
        String revisionId = (String) entity.getProperty("revisionId");
        String user = (String) entity.getProperty("userName");
        String comment = (String) entity.getProperty("comment");
        String computedAttributeString = (String) entity.getProperty("computedAttribute");
        String article = (String) entity.getProperty("parentArticle");
        String date = (String) entity.getProperty("date");
        String status = (String) entity.getProperty("status");
        String looksGoodCounter = (String) entity.getProperty("looksGoodCounter");
        String shouldReportCounter = (String) entity.getProperty("shouldReportCounter");
        String notSureCounter = (String) entity.getProperty("notSureCounter");
        try {
          JSONObject computedAttribute = (JSONObject) new JSONParser().parse(computedAttributeString); 
          editComments.add(new EditComment(revisionId, user, comment, computedAttribute.toString(), date, article, status, looksGoodCounter, shouldReportCounter, notSureCounter));
        } catch(Exception e) {
          System.out.println(e);
>>>>>>> 908f756ff1b6b081dd3e6b0aa3399bbb47d1b14a
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
   * Given a list of users, gets the revision ids of all the 
   * revision each user has made
   * @param users List of user to get their revision ids
   * @return the list of all revision ids of a specific user
   */
  public List<String> getUserIds(List<String> users, String num) {
    List<String> idList = new ArrayList<>();
    WikiMedia call = new WikiMedia();
    for (String user: users) {
      String response = call.getByUser(user, num);
      System.out.println(response);
      try {
        Object jsonObj = new JSONParser().parse(response); 
        /* Parse mockDataJSON */
        JSONObject userRevisionsObject = (JSONObject) jsonObj;
        JSONObject query = (JSONObject) userRevisionsObject.get("query");
        JSONArray revision = (JSONArray) query.get("allrevisions");
        for (Object page: revision) {
          JSONObject revisionContainer= (JSONObject) page;
          JSONArray allRevisions = (JSONArray) revisionContainer.get("revisions");
          for (Object obj: allRevisions) {
            JSONObject revisionObj = (JSONObject) obj;
            idList.add(revisionObj.get("revid").toString());
          }
        
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    System.out.println(idList);
    return idList;
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
  
    /**
   * Stores the author of the editComment in Datastore.
   * Adds the editComment's revision id to the list of revision ids of the author's editComments
   * @param EditComment editComment
   */
  private void loadUserToDatastore(EditComment editComment,DatastoreService datastore) {
    // Filter query by the Key
    Key key = KeyFactory.createKey("UserProfile", "/wikipedia/en/User:" + editComment.getUserName());
    Query query = 
      new Query("UserProfile")
        .setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
            
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();

    // Get revision ids for the existing comments by the user
    Collection<String> listEditCommentsRevids = new ArrayList<String>();
    if (entity != null){
        listEditCommentsRevids = (Collection<String>) entity.getProperty("listEditComments");
        datastore.delete(entity.getKey());
    }
    if (!listEditCommentsRevids.contains(editComment.getRevisionId())){
        listEditCommentsRevids.add(editComment.getRevisionId());
    }

    Entity userProfileEntity = new Entity("UserProfile", "/wikipedia/en/User:" + editComment.getUserName());

    userProfileEntity.setProperty("userName", editComment.getUserName());
    userProfileEntity.setProperty("listEditComments", listEditCommentsRevids);
    datastore.put(userProfileEntity);
  }

  /**
   * Given EditComment object and Datastore service, stores the edit comment into 
   * the datastore
   * @param editComment EditComment Object containing detail about the edit comment
   * @param datastore Datastore service
   */
   private void addIdToDatastore(String id, DatastoreService datastore) {
      WikiMedia call = new WikiMedia();
      String response = call.getByRevIds(Arrays.asList(id));

      // Parses response to edit comment
      EditComment editComment = call.readStringRevisionId(response).get(0);

      // Create an empty comment if comment is null
      if (editComment.comment == null || editComment.comment.equals("")) {
        editComment.comment = " ";
      }

      // Create attribute containing comment perspective api label
      Attribute attribute = new Perspective(editComment.comment, true).computedAttribute;
      editComment.toxicityObject = new Gson().toJson(attribute);

      Entity editCommentEntity = new Entity("EditComment", editComment.getRevisionId() + "en");
      editCommentEntity.setProperty("revisionId", editComment.getRevisionId());
      editCommentEntity.setProperty("userName", editComment.getUserName());
      editCommentEntity.setProperty("comment", editComment.getComment());
      editCommentEntity.setProperty("computedAttribute", editComment.getToxicityObject());
      editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
      editCommentEntity.setProperty("date", editComment.getDate());
      editCommentEntity.setProperty("status", editComment.getStatus());
      datastore.put(editCommentEntity);

      loadUserToDatastore(editComment, datastore);


   }
}