package com.google.sps.servlets; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.*;

import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;
import com.google.sps.data.WikiMedia;

import com.google.gson.Gson;

import java.io.*;
import java.lang.Math;
import java.lang.Double;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;

/** Servlet that receives and store data from the Media API*/
@WebServlet("/load-data")
public class LoadDataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    // first check id number with data

    //Get data from Media API using the WikiMedia class
    WikiMedia wikiMedia = new WikiMedia();
    String json = wikiMedia.getByRecentChanges();
    Collection<EditComment> listMockComments = wikiMedia.readWikiMediaResponse(json) ;

    // Add toxicity attributes to the comments
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments = addToxicityBreakDown(listMockComments);

    // store the new data in Datastore
    loadToDatastore(listEditComments); 
    
    // Send the JSON as the response
    response.getWriter().println(json);
  }

  /**
   * Goes through all edit comments passed though in a collection, and stores both the author and the edit comment.
   * Calls loadEditCommentToDatastore() and loadUserToDatastore()
   * @param Collection<EditComment> listEditComments
   */
  private void loadToDatastore(Collection<EditComment> listEditComments) {
    for (EditComment editComment : listEditComments) {
      loadEditCommentToDatastore(editComment);
      loadUserToDatastore(editComment);      
    }
  }

  /**
   * Stores the edit comments passed though in the Datastore if it does not already exist
   * @param EditComment editComment
   */
  private void loadEditCommentToDatastore(EditComment editComment) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("EditComment", editComment.getRevisionId() + "en");
    Query query = new Query("EditComment").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      Entity editCommentEntity = new Entity("EditComment", editComment.getRevisionId() + "en");
      editCommentEntity.setProperty("revisionId", editComment.getRevisionId());
      editCommentEntity.setProperty("userName", editComment.getUserName());
      editCommentEntity.setProperty("comment", editComment.getComment());
      editCommentEntity.setProperty("computedAttribute", editComment.getToxicityObject());
      editCommentEntity.setProperty("toxicityScore", Double.parseDouble(editComment.getToxicityScore()));
      editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
      editCommentEntity.setProperty("date", editComment.getDate());
      editCommentEntity.setProperty("status", editComment.getStatus());
      editCommentEntity.setProperty("looksGoodCounter", editComment.getLooksGoodCounter());
      editCommentEntity.setProperty("shouldReportCounter", editComment.getShouldReportCounter());
      editCommentEntity.setProperty("notSureCounter", editComment.getNotSureCounter());
      datastore.put(editCommentEntity);
    }
  }

  /**
   * Stores the author of the editComment in Datastore.
   * Adds the editComment's revision id to the list of revision ids of the author's editComments
   * @param EditComment editComment
   */
  private void loadUserToDatastore(EditComment editComment) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
   * Goes through all edit comments passed though in a collection without a toxicity attribute,
   * and add a toxicity attribute to each one by using the Perspective class
   * @param Collection<EditComment> listMockComments
   * @return Collection<EditComment> containing EditComments with toxicity attributes
   */
  private Collection<EditComment> addToxicityBreakDown(Collection<EditComment> listMockComments) {
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();
    
    for (EditComment comment : listMockComments){
      // Check first if comment is in the Datastore
      // Skip the comment if it is in the Datastore
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      // Filter query by the Key
      Key key = KeyFactory.createKey("EditComment", comment.getRevisionId() + "en");
      Query query = new Query("EditComment").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
      PreparedQuery results = datastore.prepare(query);
      Entity entity = results.asSingleEntity();
      if (entity != null) continue;

      // Check if comment is empty
      // if the comment is empty, set the toxicityObject to 0, skip the call to the Perspective API,
      // and add to the list to be stored in Datastore
      if (comment.getComment().trim().isEmpty()) {
          comment.setToxicityObject("0");
          listEditComments.add(comment);
          continue;
      }


      Attribute attribute = new Perspective(comment.getComment(), true).computedAttribute;
      Gson gson = new Gson();
      String attributeString = gson.toJson(attribute);
      comment.setToxicityObject(attributeString);
      listEditComments.add(comment);
      try{
        Thread.sleep(1000);
      }
      catch(InterruptedException ex)
      {
        Thread.currentThread().interrupt();
      }
    }
    return listEditComments;
  }
}