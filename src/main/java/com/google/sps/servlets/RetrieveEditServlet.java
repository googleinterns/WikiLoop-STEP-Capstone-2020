package com.google.sps.servlets;

//datastore
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;

//classes
import com.google.sps.data.EditComment;

//servlet
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//get comment id based on what is clicked in discover page
@WebServlet("/retrieve")
public class RetrieveEditServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //should give the comment with the same ID/Key as the request
      /*
      Query query = new Query("EditComment").setFilter(FilterPredicate(Entity.KEY_RESERVED_PROPERTY, 
      FilterOperator.EQUAL, id));
      PreparedQuery pq = datastore.prepare(query);
      Entity results = pq.asSingleEntity();

      String editComment = request.getParameter('editCommentText');

      Gson gson = new Gson();
      String json = gson.toJson(edit);
      response.setContentType("application/json");
      response.getWriter().println(gson.toJson(json));
      */

      //key from datastore 
    //  Entity editComment = datastore.get(key);
      
    }        
}