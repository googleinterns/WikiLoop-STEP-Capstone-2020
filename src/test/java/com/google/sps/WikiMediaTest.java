import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import javax.servlet.http.*;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.google.sps.servlets.DiscoverServlet;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.sps.tests.MockData;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import org.junit.Assert;
import com.google.sps.data.Perspective;
import com.google.sps.tests.MockData;
import com.google.gson.Gson;
import com.google.sps.data.WikiMedia;
import com.google.sps.data.EditComment;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * This class tests the WikiMedia API class which is responsible 
 * for query comments from Wikipedia. The WikiMedia class can get revision
 * comments from Wikipedia by revision id, user name, recent changes, etc
 */
public class WikiMediaTest extends Mockito {
  private WikiMedia call = new WikiMedia();
  /**
   * This tests the WikiMedia API by getting a revision comment by id
   */
  @Test
  public void getCommentByRevid() throws IOException {
    String id = "283416010";
    String response = call.getByRevIds(Arrays.asList(id));
    // Parses response to edit comment
    EditComment editComment = call.readStringRevisionId(response).get(0);
    String responseString = new Gson().toJson(editComment);
    String testResponse = "{\"userName\":\"Giano II\",\"comment\":\" what the fuck use is that?\",\"date\":\"2009-04-12T19:57:15Z\",\"parentArticle\":\"Wikipedia:Administrators\u0027 noticeboard/Incidents\",\"status\":\"NEW\",\"revisionId\":\"283416010\",\"toxicityObject\":\"0\",\"toxicityScore\":\"0.0\",\"looksGoodCounter\":\"0\",\"shouldReportCounter\":\"0\",\"notSureCounter\":\"0\"}";
    try {
    JSONParser parser = new JSONParser();
    JSONObject jsonTest = (JSONObject) parser.parse(testResponse);
    JSONObject jsonActual = (JSONObject) parser.parse(responseString);
    assertEquals(jsonTest, jsonActual);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * This tests the WikiMedia API function of getting a revision comment by user name
   * if the user adds a new revision this test will fail. Therefore, the test response
   * would have to be updated
   */
  @Test
  public void getCommentByUser() throws IOException {
    String user = "download";
    String response = call.getByUser(user, "1");
    String id = "";
      // Parse WikiMedia call to get revision ids of specific user
      try {
        Object jsonObj = new JSONParser().parse(response); 
        JSONObject userRevisionsObject = (JSONObject) jsonObj;
        JSONObject query = (JSONObject) userRevisionsObject.get("query");
        JSONArray revision = (JSONArray) query.get("allrevisions");
        for (Object page: revision) {
          JSONObject revisionContainer= (JSONObject) page;
          JSONArray allRevisions = (JSONArray) revisionContainer.get("revisions");
          for (Object obj: allRevisions) {
            JSONObject revisionObj = (JSONObject) obj;
            id = (revisionObj.get("revid").toString());
          }
        }
      } catch (Exception e) {
        System.out.println("getUserIds " +e);
      }
    try {
      String responseId = call.getByRevIds(Arrays.asList(id));
      // Parses response to edit comment
      EditComment editComment = call.readStringRevisionId(responseId).get(0);
      String responseString = new Gson().toJson(editComment);
      System.out.println(responseString);
      String testResponse = "{\"userName\":\"Download\",\"comment\":\" rm redlink\",\"date\":\"2017-11-15T23:45:12Z\",\"parentArticle\":\"Q-learning\",\"status\":\"NEW\",\"revisionId\":\"810552631\",\"toxicityObject\":\"0\",\"toxicityScore\":\"0.0\",\"looksGoodCounter\":\"0\",\"shouldReportCounter\":\"0\",\"notSureCounter\":\"0\"}";
      JSONParser parser = new JSONParser();
      JSONObject jsonTest = (JSONObject) parser.parse(testResponse);
      JSONObject jsonActual = (JSONObject) parser.parse(responseString);
      assertEquals(responseString, testResponse);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}