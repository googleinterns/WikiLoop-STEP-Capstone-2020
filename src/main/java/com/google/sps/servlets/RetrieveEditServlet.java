package com.google.sps.servlets;

import java.util.ArrayList;
import com.google.gson.Gson;

//datastore
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

//classes
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
//import com.google.sps.data.MockComment;

//servlet
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//get comment based on what is clicked in discover page
// TO DO: Link with Datastore
@WebServlet("/retrieve")
public class RetrieveEditServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //retrieve edit information from discover page

      // Get Edit Comment ID
      //long revisionId = long(request.getParameter("revivionId"));

      /* Get Edit from Datastore using ID
      Query query = new Query("EditComment");
      PreparedQuery pq = datastore.prepare(query);

      ArrayList<EditComment> ec = new ArrayList();
      for (Entity e : pq.asIterable()) {
        long id = e.getKey().getId();
          if (revisionId == id) {
            String userName = (String) e.getProperty("userName");
            String comment = (String) e.getProperty("comment");
            String date = (String) e.getProperty("date");
            String parentArticle = (String) e.getProperty("parentArticle");
            String status = (String) e.getProperty("status");
            String toxicityObject = (String) e.getProperty("toxicityObject");
          } else { continue };
      }
      */

      //mock
      String id = "861223655";
      EditComment edit = retrieveEdit(id);
      //EditComment edit = request.getParameter("edit", edit); //change

      Gson gson = new Gson();

      response.setContentType("application/json");
      response.getWriter().println(gson.toJson(edit));
    }  

    /* TO DO: Use Datastore */
    private EditComment retrieveEdit(String id) {
      // Create arraylist with mocked data
      ArrayList<EditComment> list = new ArrayList<EditComment>();
      EditComment edit1 = new EditComment("861223655", "K6ka",
      "Your explanation on the talk page is completely ludicrous.",
      "74%", "September 25, 2018 23:40", "https://en.wikipedia.org/w/index.php?title=Incivility",
      "None");
      EditComment edit2 = new EditComment("758943201", "Tom",
      "Your comment is pretty ignorant.","83.79%", "September, 5 2019, 12:40", 
      "https://en.wikipedia.org/w/index.php?title=Incivility", "None");

      EditComment edit3 = new EditComment("135138032", "Jerry", 
      "Your are the worst!","66.55%", "September, 6 2019, 21:09",
      "https://en.wikipedia.org/w/index.php?title=Incivility", "None");

      list.add(edit1);
      list.add(edit2);
      list.add(edit3);

      // Create the edit that will be returned
      EditComment edit;

      // Iterate through array of EditComments, find one with same ID
      for(EditComment e : list) {
        if (e.getRevisionId() == id) {
            edit = e;
        }
      }
      return edit1;
    }   
}