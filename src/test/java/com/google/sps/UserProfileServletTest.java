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

  //private static final EditComment ec1 = new EditComment("283416010", "Giano II", "/* Comments */ what the fuck use is that?",
 //   "91", "2009-04-12T19:57:15Z", "Wikipedia:Administrators' noticeboard/Incidents", "pending");
 /// private static final EditComment ec2 = new EditComment("283242467", "Giano II", "/* Please... */ Oh do go away and take your nasty, little Admin habits with you. Be a man and do something useful with your time, have a chat with Neurolysis and accept another apology on my behalf, a",
 //   "68", "2009-04-11T20:58:39Z", "User talk:Giano II", "pending");
 // private static final EditComment ec3 = new EditComment("290490251", "Giano II", "/* Discussion */ If she wants to chat on IRC - fine, but don't come here wanting to be an admin on the strength of it.", "32", "2009-05-17T11:41:52Z", "Wikipedia:Requests for adminship/FlyingToaster 2",
  //  "pending");
 // private static final EditComment ec4 = new EditComment("290692859", "Giano II", "/* Oppose */ accept it and stop badgering! ~~~~",
  //  "32", "2009-05-18T11:13:20Z", "Wikipedia:Requests for adminship/FlyingToaster 2", "pending");
 /// private static final EditComment ec5 = new EditComment("290547577", "Giano II", "/* Time now for 2groups of editors each with their own Admins. */ That the chanel is for the banal is not in dispute, the problem is when the banal decide they are bored there, and having done little",
  //  "12", "2009-05-17T17:46:35Z", "User talk:Giano II", "pending");
 // private static final EditComment ec6 = new EditComment("974409544", "WomenArtistUpdates", "* Continental Challenge Draft Barnstar */ ce",
 //   "28", "2020-08-22T21:20:37Z", "Wikipedia:WikiProject Women in Red/Ideas", "pending");
 // private static final EditComment ec7 = new EditComment("974409546", "ProcrastinatingReader", "Convert usage to Infobox event, per TfD	",
 //   "13", "2020-08-22T21:20:37Z", "User:Rocpuigmal/Murder of Helena Jubany", "pending");
 // private static final EditComment ec8 = new EditComment("974409548", "Omnipaedista", "/* Contributions */",
  //  "4", "2020-08-22T21:20:38Z", "User:Omnipaedista", "pending");
  //private static final EditComment ec9 = new EditComment("974409549", "Bmf 051", "Caution: Addition of unsourced or improperly cited material on [[:MTV Big F]]. ([[WP:TW|TW]])",
 //   "21", "2020-08-22T21:20:39Z", "User talk:2409:4042:218D:37B8:AFDA:4DE6:7FE7:B933", "pending");
 // private static final EditComment ec10 = new EditComment("974409555", "S0091", "* Draft:We Are Lebanon */ new section",
 //   "7", "2020-08-22T21:20:41Z", "User talk:Mirna Srour Srour", "pending");

  private static final String USER_KEY_1 = "/wikipedia/en/User:Giano II";
  private static final String USER_KEY_2 = "/wikipedia/en/User:WomenArtistUpdates";
  private static final String USER_KEY_3 = "/wikipedia/en/User:ProcrastinatingReader";
  private static final String USER_KEY_4 = "/wikipedia/en/User:Omnipaedista";
  private static final String USER_KEY_5 = "/wikipedia/en/User:Bmf 051";
  private static final String USER_KEY_6 = "/wikipedia/en/User:S0091";
  private static final String USER_KEY_7 = "/wikipedia/en/User:DeltaQuadBot";

  private static final String USER_NAME_1 = "Giano II";
  private static final String USER_NAME_2 = "WomenArtistUpdates";
  private static final String USER_NAME_3 = "ProcrastinatingReader";
  private static final String USER_NAME_4 = "Omnipaedista";
  private static final String USER_NAME_5 = "Bmf 051";
  private static final String USER_NAME_6 = "S0091";
  private static final String USER_NAME_7 = "DeltaQuadBot";


  @Before
  public void setUp() {
    helper.setUp();
    //loadToDatastore();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void returnValidAndCorrectJSONString() throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    

    when(request.getParameter("timeFrame")).thenReturn("0");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new UserProfileServlet().doGet(request, response);
    verify(request, atLeast(1)).getParameter("timeFrame"); 
    writer.flush();
    // Strip the last char which is a new line
    System.out.println(stringWriter.toString());
    }

    /**
   * Goes through all edit comments passed though in a collection, and stores both the author and the edit comment.
   * Calls loadEditCommentToDatastore() and loadUserToDatastore()
   * @param Collection<EditComment> listEditComments
   */ /*
  private void loadToDatastore() {
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments.add(ec1);
    listEditComments.add(ec2);
    listEditComments.add(ec3);
    listEditComments.add(ec4);
    listEditComments.add(ec5);
    listEditComments.add(ec6);
    listEditComments.add(ec7);
    listEditComments.add(ec8);
    listEditComments.add(ec9);
    listEditComments.add(ec10);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    for (EditComment editComment : listEditComments) {
      loadEditCommentToDatastore(editComment);
      loadUserToDatastore(editComment);      
    }
  }
*/
  /**
   * Stores the edit comments passed though in the Datastore if it does not already exist
   * @param EditComment editComment
   */ /*
  private void loadEditCommentToDatastore(EditComment editComment) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("EditComments", editComment.getRevisionId() + "en");
    Query query = new Query("EditComments").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      Entity editCommentEntity = new Entity("EditComments", editComment.getRevisionId() + "en");
      editCommentEntity.setProperty("revisionId", editComment.getRevisionId());
      editCommentEntity.setProperty("userName", editComment.getUserName());
      editCommentEntity.setProperty("comment", editComment.getComment());
      editCommentEntity.setProperty("computedAttribute", editComment.getToxicityObject());
      editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
      editCommentEntity.setProperty("date", editComment.getDate());
      editCommentEntity.setProperty("status", editComment.getStatus());
      datastore.put(editCommentEntity);
    }
  } */

  /**
   * Stores the author of the editComment in Datastore.
   * Adds the editComment's revision id to the list of revision ids of the author's editComments
   * @param EditComment editComment
   */ /*
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
  } */
}