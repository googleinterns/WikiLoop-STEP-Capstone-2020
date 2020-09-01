
package com.google.sps.servlets;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.data.EditComment;

import com.google.gson.Gson;

import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException; 

/** Servlet that returns UserProfile information */
@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    String timeFrameString = request.getParameter("timeFrame");
    String userNameString = request.getParameter("User");
    String userName = "Giano II";

    if (userNameString != null && !(userNameString.equals("")) && !(userNameString.equals("null")) && !(userNameString.equals("undefined"))) {
      userName = userNameString;
    }
    int timeFrame = 0;
    if (timeFrameString != null && timeFrameString != "") timeFrame = Integer.parseInt(timeFrameString);

    
    // Get User's instance from Datastore
    User user = retrieveUser(userName, timeFrame);

    // Jasonify the User (Convert the userprofile to JSON)
    Gson gson = new Gson();
    String json = gson.toJson(user);
    
    // Send the JSON as the response
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Redirect back to the HTML page.
    response.sendRedirect("index.html");
  }

  /** 
   * Retrieve a user with a given name from Datastore
   * @param String userToRetrieve
   * @return User
   */
  private User retrieveUser(String userToRetrieve, int timeFrame) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Filter query by the Key
    Key key = KeyFactory.createKey("UserProfile", "/wikipedia/en/User:" + userToRetrieve);
    Query query = 
        new Query("UserProfile")
            .setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
            
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null){
        return null;
    }
    String id = String.valueOf(entity.getKey().getId());
    String userName = (String) entity.getProperty("userName");

    // Get User's edit comments revision ids
    Collection<String> listEditCommentsRevids = new ArrayList<String>();
    listEditCommentsRevids = (Collection<String>) entity.getProperty("listEditComments");
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    listEditComments = retrieveUserEditComments(listEditCommentsRevids, timeFrame);
    User user = new User(id, userName, listEditComments);
    return user;
  }

  /**
   * Goes through the collection of revision ids and returns a collectino of EditComments retrieved from Datastore,
   * with the corresponding revision ids
   * @param Collection<String> listEditCommentsRevids
   * @return ArrayList<EditComment> of EditComments
   */
  private ArrayList<EditComment> retrieveUserEditComments(Collection<String> listEditCommentsRevids, int timeFrame) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    for (String revid : listEditCommentsRevids) {
        // Filter query by the Key
        Key key = KeyFactory.createKey("EditComment", revid + "en");
        Query query = new Query("EditComment")
            .setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));

        PreparedQuery results = datastore.prepare(query);
        Entity entity = results.asSingleEntity();

        String userName = (String) entity.getProperty("userName");
        String comment = (String) entity.getProperty("comment");
        String dateString = (String) entity.getProperty("date");
        String parentArticle = (String) entity.getProperty("parentArticle");
        String status = (String) entity.getProperty("status");
        String toxicityObject = (String) entity.getProperty("computedAttribute");
        String revisionId = (String) entity.getProperty("revisionId");
        String looksGoodCounter = (String) entity.getProperty("looksGoodCounter");
        String shouldReportCounter = (String) entity.getProperty("shouldReportCounter");
        String notSureCounter = (String) entity.getProperty("notSureCounter");

        if (timeFrame == 0 || isInTimeFrame(dateString, timeFrame)) listEditComments.add(new EditComment(revisionId, userName, comment, toxicityObject, dateString, parentArticle, status, looksGoodCounter, shouldReportCounter, notSureCounter));
    }
        
    return listEditComments;
  }

  /** 
   * Retrieve a boolean for whether a date is in a certain time frame from the current date
   * @param String editCommentDate
   * @param in timeFrameInDays
   * @return boolean
   */
  private boolean isInTimeFrame (String editCommentDate, int timeFrameInDays) {

    long timeFrameInMilliSeconds = (long) timeFrameInDays * 86400000;
    long currentTimeMilliSeconds = System.currentTimeMillis();
    long editCommentDateInMilliSeconds = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    try {
      Date dateTime = sdf.parse(editCommentDate);
      editCommentDateInMilliSeconds = dateTime.getTime();
      return editCommentDateInMilliSeconds > (currentTimeMilliSeconds - timeFrameInMilliSeconds);
    } catch (Exception e){System.out.println(e);}
      return false;
  }
}