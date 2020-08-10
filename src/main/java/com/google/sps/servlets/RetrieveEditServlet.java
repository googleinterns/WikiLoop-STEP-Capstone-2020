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
import com.google.sps.data.MockComment;
import com.google.sps.data.Attribute;

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
      String id = getIdFromUrl(request);

      EditComment edit = retrieveEdit(id);

      Gson gson = new Gson();

      response.setContentType("application/json");
      response.getWriter().println(gson.toJson(edit));
    }  

    private String getIdFromUrl(HttpServletRequest request) {
        System.out.println(request.getQueryString());
        return request.getParameter("id");
    }

    /* TO DO: Use Datastore */
    private EditComment retrieveEdit(String id) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query("EditComment").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
      PreparedQuery pq = datastore.prepare(query);

      Entity entity = pq.asSingleEntity();

      String userName = (String) entity.getProperty("userName");
      String comment = (String) entity.getProperty("comment");
      String date = (String) entity.getProperty("date");
      String parentArticle = (String) entity.getProperty("parentArticle");
      String status = (String) entity.getProperty("status");
      String revisionID = (String) entity.getProperty("revisionID");
      String toxicityObject = (String) entity.getProperty("toxicityObject");

      EditComment ec = new EditComment(revisionID, userName, comment, toxicityObject, date, parentArticle, status);

      return ec;
    }   
}