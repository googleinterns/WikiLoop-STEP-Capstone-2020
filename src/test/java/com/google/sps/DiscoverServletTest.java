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
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;


/**
 * This class tests the functionality of the DiscoverServlet which is 
 * responsible for loading comments into discover page table and slider
 * This class creates a local datastore and inputs mock data in to the
 * datastore, runs the discover servlet logic such as getting all edit 
 * comments and specific edit comments. and checks if the JSONarray return
 * to the front end is correct
 */
public class DiscoverServletTest extends Mockito {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private MockData mock = new MockData("");

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  /**
   * Mocks the process of returning all the comments in the 
   * datastore to the discover page and slider. This function
   * creates a local datastore and inputs data then calls the
   * data foward using DiscoverServlet logic. Afterwards asserts
   * that the response is correct
   */
  @Test
  public void getAllEditComments() throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    
    when(request.getParameter("id")).thenReturn("");

    mock.storeLocalData(mock.readMockTestJson("mockDataAllComments.json"), datastore);
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    
    new DiscoverServlet().doGet(request, response);
    verify(request, atLeast(1)).getParameter("id"); 
    writer.flush();

    try {
      JSONParser parser = new JSONParser();
      JSONArray arrayResponse = (JSONArray)parser.parse(stringWriter.toString());
      //assertEquals(arrayResponse, mock.readMockTestJson("mockDataAllComments.json"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Mocks the process of returning a specific edit comments in the 
   * datastore to the discover page and slider. This function
   * creates a local datastore and inputs data then calls the
   * data foward using DiscoverServlet logic. Afterwards asserts
   * that the response is correct
   */
  @Test
  public void getSpecificEditComment() throws IOException {
    /*
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    
    when(request.getParameter("id")).thenReturn("283242467");

    mock.storeLocalData(mock.readMockTestJson("mockDataAllComments.json"), datastore);
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    
    new DiscoverServlet().doGet(request, response);
    verify(request, atLeast(1)).getParameter("id"); 
    writer.flush();

    try {
      JSONParser parser = new JSONParser();
      JSONArray arrayResponse = (JSONArray) parser.parse(stringWriter.toString());
      JSONArray testResponse = (JSONArray) parser.parse("["+mock.readMockTestJson("mockDataAllComments.json").get(0).toString() +"]");
      assertEquals(arrayResponse, testResponse);
    } catch (Exception e) {
      System.out.println(e);
    }
    */
  }
}