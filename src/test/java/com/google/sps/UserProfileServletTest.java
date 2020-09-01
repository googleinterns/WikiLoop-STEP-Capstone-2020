package com.google.sps.test;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.data.EditComment;
import com.google.sps.data.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.sps.servlets.UserProfileServlet;

import com.google.appengine.api.datastore.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import javax.servlet.http.*;
import org.apache.commons.io.FileUtils;
 
import org.apache.commons.lang.builder.EqualsBuilder;


public class UserProfileServletTest {
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private static final EditComment ec1 = new EditComment("283242467", "Giano II", " Oh do go away and take your nasty, little Admin habits with you. Be a man and do something useful with your time, have a chat with Neurolysis and accept another apology on my behalf, a",
    "{\"toxicityScore\":\"69\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"TOXICITY\",\"score\":\"69\",\"reason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"experimental\":false}",
     "2009-04-11T20:58:39Z", "User talk:Giano II", "NEW", "0", "0", "0");

  private static final EditComment ec2 = new EditComment("283242467", "Giano II", " what the fuck use is that?",
    "{\"toxicityScore\":\"84\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"PROFANITY\",\"score\":\"91\",\"reason\":\"Swear words, curse words, or other obscene or profane language\",\"experimental\":false}",
     "2009-04-12T19:57:15Z", "User talk:Giano II", "NEW", "0", "0", "0");

  private static final EditComment ec3 = new EditComment("283242467", "Giano II", "accept it and stop badgering! ~~~~",
    "{\"toxicityScore\":\"37\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"FLIRTATION\",\"score\":\"41\",\"reason\":\"Pickup lines, complimenting appearance, subtle sexual innuendos, etc\",\"experimental\":true}",
     "2009-05-18T11:13:20Z", "User talk:Giano II", "NEW", "0", "0", "0");

  private static final EditComment ec4 = new EditComment("967664593", "Bastun", "WTF? Now you're overwriting my comments?!",
    "{\"toxicityScore\":\"78\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"PROFANITY\",\"score\":\"85\",\"reason\":\"Swear words, curse words, or other obscene or profane language\",\"experimental\":false}",
     "2020-07-14T15:05:13Z", "Wikipedia:Move review/Log/2020 July", "NEW", "0", "0", "0");

  private static final EditComment ec5 = new EditComment("968857509", "Bastun", " Ok, I get reordering lists arbitrarily seems to be your thing, but stop now?",
    "{\"toxicityScore\":\"12\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"THREAT\",\"score\":\"27\",\"reason\":\"Describes an intention to inflict pain, injury, or violence against an individual or group\",\"experimental\":false}",
     "2020-07-21T23:02:43Z", "Social Democrats (Ireland)", "NEW", "0", "0", "0");

  private static final EditComment ec6 = new EditComment("975316313", "Bastun", "redlink",
    "{\"toxicityScore\":\"3\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"FLIRTATION\",\"score\":\"11\",\"reason\":\"Pickup lines, complimenting appearance, subtle sexual innuendos, etc\",\"experimental\":true}",
     "2020-08-27T21:08:13Z", "Irish Freedom Party", "NEW", "0", "0", "0");

  private static final EditComment ec7 = new EditComment("975336175", "Bastun", "r",
    "{\"toxicityScore\":\"3\",\"toxicityReason\":\"Rude, disrespectful, or unreasonable comment that is likely to make people leave a discussion\",\"label\":\"FLIRTATION\",\"score\":\"11\",\"reason\":\"Pickup lines, complimenting appearance, subtle sexual innuendos, etc\",\"experimental\":true}",
     "2020-08-27T22:57:29Z", "Talk:Oireachtas Golf Society scandal", "NEW", "0", "0", "0");

  private static final String USER_KEY_1 = "/wikipedia/en/User:Giano II";
  private static final String USER_KEY_2 = "/wikipedia/en/User:Bastun";

  private static final String USER_NAME_1 = "Giano II";
  private static final String USER_NAME_2 = "Bastun";

  private static final String ALL_TIME_RANGE_STRING = "0";
  private static final String LAST_YEAR_RANGE_STRING = "365";
  private static final String LAST_MONTH_RANGE_STRING = "30";
  private static final String LAST_WEEK_RANGE_STRING = "7";
  private static final String LAST_24_HOURS_RANGE_STRING = "1";


  @Before
  public void setUp() {
    helper.setUp();
    loadToDatastore();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void returnCorrectJSONString() throws IOException {
    // Test whether the servlet retrieves the right user


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    

    when(request.getParameter("timeFrame")).thenReturn(ALL_TIME_RANGE_STRING);
    when(request.getParameter("User")).thenReturn(USER_NAME_2);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new UserProfileServlet().doGet(request, response);
    writer.flush();


    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments.add(ec4);
    listEditComments.add(ec5);
    listEditComments.add(ec6);
    listEditComments.add(ec7);

    Gson gson = new Gson();
    User user = new User("0", USER_NAME_2, listEditComments);
    String expected = gson.toJson(user);

    String actual = stringWriter.toString();
    // Strip the last char which is a new line
    actual = actual.substring(0, actual.length() - 1);

    assertEquals(expected, actual);
    }

    @Test
  public void returnCorrectTimeFrame() throws IOException {
    // Test whether the servlet retrieves comments from the right timeFrame
    // Should no comment as Bastun does not have a comment in the last 24 hours from 
    // the current time

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    

    when(request.getParameter("timeFrame")).thenReturn(LAST_24_HOURS_RANGE_STRING);
    when(request.getParameter("User")).thenReturn(USER_NAME_2);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new UserProfileServlet().doGet(request, response);
    writer.flush();


    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();

    Gson gson = new Gson();
    User user = new User("0", USER_NAME_2, listEditComments);
    String expected = gson.toJson(user);

    String actual = stringWriter.toString();
    // Strip the last char which is a new line
    actual = actual.substring(0, actual.length() - 1);

    //System.out.println(actual);
    assertEquals(expected, actual);
    }

    /**
   * Goes through all edit comments passed though in a collection, and stores both the author and the edit comment.
   * Calls loadEditCommentToDatastore() and loadUserToDatastore()
   * @param Collection<EditComment> listEditComments
   */
  private void loadToDatastore() {
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments.add(ec1);
    listEditComments.add(ec2);
    listEditComments.add(ec3);
    listEditComments.add(ec4);
    listEditComments.add(ec5);
    listEditComments.add(ec6);
    listEditComments.add(ec7);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
}