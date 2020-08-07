package com.google.sps.tests;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
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

  private final List<EditComment> listMockComments;
  private final JSONArray expectedResponse;

  public MockData(String json) {
    this.listMockComments = this.readMockJson(json);
    this.expectedResponse = this.readMockTestJson();
  }
  
  /**
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<MockComment> readMockJson(String json) {
    ArrayList<MockComment> mockComments = new ArrayList<MockComment>();


    /* Reads JSON file & converts to edit comment */
    try { 
      Object jsonObj = new JSONParser().parse(json); 

      /* Parse mockDataJSON */
      JSONObject mockDataObject = (JSONObject) jsonObj;
      JSONObject query = (JSONObject) mockDataObject.get("query");
      JSONArray allrevisions = (JSONArray) query.get("allrevisions");

      /*Iterate and Parse each revision, create mock comments */
      for (Object o : allrevisions) {
          JSONObject comment = (JSONObject) o;
          String pageId = String.valueOf(comment.get("pageid"));
          JSONArray revision = (JSONArray) comment.get("revisions");
          String article = (String) comment.get("title");
          for (Object obj : revision) {
              JSONObject object = (JSONObject) obj;
              String revisionId = String.valueOf(object.get("revid"));
              String user = (String) object.get("user");
              String mockComment = (String) object.get("comment");
              String date = (String) object.get("timestamp");

              mockComments.add(new MockComment(revisionId, user, mockComment, date, article));
          }
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return editComments;
  }
  
  /**
   * Reads the mockDataTest.json file in project directory which simulates end product of 
   * DiscoverServlet. This function reads the mockDataTest.json file and returns JSONArry
   * of information in order to compare within the DataServletTest file
   * @return JSONArray of EditCommentObjects
   */
  private JSONArray readMockTestJson() {
    /* Read Json file */
    try { 
      Object obj = new JSONParser().parse(new FileReader("mockDataTest.json")); 
      JSONArray comment = (JSONArray) obj;
      return comment;
    } catch (Exception e) {
      System.out.println(e);
    }
      return new JSONArray();
    }

  public List<EditComment> getMockComments() {
    return listMockComments;
  }

  public JSONArray getExpectedResponse() {
    return expectedResponse;
  }
  

}
