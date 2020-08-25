package com.google.sps.tests;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.FileReader; 
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

/** 
 * Class for testing data going through to the Discover page. This class is responsible for 
 * reading mockData & mockDataTest json files in project directory and comparing the result
 * of the mock data when converted to edit comment objects to mockDataTest (list of edit comment objects)
 **/
public final class MockData {

  private final Collection<EditComment> listMockComments;
  //private final JSONArray expectedResponse;

  public MockData(String json) {
    this.listMockComments = this.readMockJson(json);
    //this.expectedResponse = this.readMockTestJson();
  }
  
  /**
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<EditComment> readMockJson(String json) {
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
              editComments.addAll(readMockJsonByRevisionID((JSONObject) query.get(queryType)));
          }
          else if (queryType.equals("allrevisions")) {
              editComments.addAll(readMockJsonByAllRevisions((JSONArray) query.get(queryType)));
          }
          else if (queryType.equals("recentchanges")) {
              System.out.println("RECENT CHANGES");
              editComments.addAll(readMockJsonByRecentChanges((JSONArray) query.get(queryType)));
          }
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return editComments;
  }

  /**
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<EditComment> readMockJsonByRevisionID(JSONObject pages) {
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
<<<<<<< HEAD
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<EditComment> readMockJsonByAllRevisions(JSONArray allrevisions) {
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
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<EditComment> readMockJsonByRecentChanges(JSONArray recentchanges) {
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
  
   /* Given a JSONArray of edit comments function stores the edit comments in a local
   * datastore used for testing
   * @param editcomments JSON array of EditComments
   */
  public void storeLocalData(JSONArray editComments, DatastoreService datastore) {
    for (Object editCommentObj: editComments) {
      JSONObject editComment = (JSONObject) editCommentObj;
      Entity editCommentEntity = new Entity("EditComments", (String) editComment.get("revisionId") + "en");
      editCommentEntity.setProperty("revisionId", (String) editComment.get("revisionId"));
      editCommentEntity.setProperty("userName", (String) editComment.get("userName"));
      editCommentEntity.setProperty("comment", (String) editComment.get("comment"));
      editCommentEntity.setProperty("computedAttribute", (String) editComment.get("toxicityObject"));
      editCommentEntity.setProperty("parentArticle", (String) editComment.get("parentArticle"));
      editCommentEntity.setProperty("date", (String) editComment.get("date"));
      editCommentEntity.setProperty("status", (String) editComment.get("status"));
      datastore.put(editCommentEntity);
    }
  }

  public Collection<EditComment> getMockComments() {
    return listMockComments;
  }

  /**
   * Reads the mockDataTest.json file in project directory which simulates end product of 
   * DiscoverServlet. This function reads the mockDataTest.json file and returns JSONArry
   * of information in order to compare within the DataServletTest file
   * @return JSONArray of EditCommentObjects
   */
  public JSONArray readMockTestJson(String filename) {
    // Read Json file 
    try { 
      Object obj = new JSONParser().parse(new FileReader(filename)); 
      JSONArray comment = (JSONArray) obj;
      return comment;
    } catch (Exception e) {
      System.out.println(e);
    }
      return new JSONArray();
    } 

  /**
   * Reads the mockDataTest.json file in project directory which simulates end product of 
   * DiscoverServlet. This function reads the mockDataTest.json file and returns JSONArry
   * of information in order to compare within the DataServletTest file
   * @return JSONArray of EditCommentObjects
   */
  public JSONObject readMockPerspective(String filename) {
    // Read Json file 
    try { 
      Object obj = new JSONParser().parse(new FileReader(filename)); 
      JSONObject comment = (JSONObject) obj;
      return comment;
    } catch (Exception e) {
      System.out.println(e);
    }
      return new JSONObject();
    } 

/*
  public JSONArray getExpectedResponse() {
    return expectedResponse;
  } */
  
}
