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

/** 
 * Class for testing data going through to the Discover page. This class is responsible for 
 * reading mockData & mockDataTest json files in project directory and comparing the result
 * of the mock data when converted to edit comment objects to mockDataTest (list of edit comment objects)
 **/
public final class MockData {

  private final List<EditComment> listMockComments;
  private final JSONArray expectedResponse;

  public MockData() {
    this.listMockComments = this.readMockJson();
    this.expectedResponse = this.readMockTestJson();
  }
  
  /**
   * Read the mockData.json file in project directory which simulates WikiMedia api response of a query
   * for edit comment information of a wiki article. This function converts the json into a list of mock
   * comments for testing
   * @return List<MockComments>
   */
  private List<EditComment> readMockJson() {
    ArrayList<EditComment> editComments = new ArrayList<EditComment>();
    
    
    /*
    Entity commentEntity = new Entity("Commnent");
      commentEntity.setProperty("sentimentScore", String.valueOf(score));
      commentEntity.setProperty("text", comment);
      commentEntity.setProperty("userEmail", userEmail);
      commentEntity.setProperty("time", postedTime);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);*/

    /* Reads JSON file & converts to edit comment */
    try { 
      Object obj = new JSONParser().parse(new FileReader("mockData.json")); 

      /* Parse mockDataJSON */
      JSONObject mockDataObject = (JSONObject) obj;
      JSONObject query = (JSONObject) mockDataObject.get("query");
      JSONObject pages = (JSONObject) query.get("pages");
      JSONObject pageId = (JSONObject) pages.get("876262");
      String article = (String) pageId.get("title");
      JSONArray revisions = (JSONArray) pageId.get("revisions");

      /* Iterate & Parse article revisions, create mock comments*/
      for (Object o: revisions) {
        JSONObject comment = (JSONObject) o;
        String revisionId = String.valueOf(comment.get("revid"));
        String user = (String) comment.get("user");
        String mockComment = (String) comment.get("comment");
        String date = (String) comment.get("timestamp");
        editComments.add(new EditComment(revisionId, user, mockComment, "0", date, article, "NEW"));
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
