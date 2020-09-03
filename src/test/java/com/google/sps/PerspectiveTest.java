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

/**
 * This class tests the Perspective API class which is responsible 
 * for calling the perspective api and handling the response, which 
 * ranks the text label by highest probability. These tests deal with
 * experimental machine learning models, thus toxicity score values are likely
 * to change & current existing experimental models might go into production which will
 * change the expected response
 */
public class PerspectiveTest extends Mockito {
  private MockData mock = new MockData("");
  /**
   * This tests the attribute response that that desired after the Perspective class
   * call the Perspective API and ranks all the attribute labels by highest probability.
   * then returns the toxicity score, the toxicity desc, and a potential experimental label and reason
   */
  @Test
  public void getPerspectiveResponse() throws IOException {
    Perspective perspective = new Perspective("/* Comments */ what the fuck use is that?", true);
    Gson gson = new Gson();
    String attributeString = gson.toJson(perspective.computedAttribute);
    try {
      JSONParser parser = new JSONParser();
      JSONObject json = (JSONObject) parser.parse(attributeString);
      assertEquals(json, mock.readMockPerspective("mockDataAttributeResponse.json"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}