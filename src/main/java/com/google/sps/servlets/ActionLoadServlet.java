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
import com.google.sps.data.Attribute;
import com.google.sps.data.Action;


//servlet
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//get comment based on what is clicked in discover page
// TO DO: Link with Datastore

@WebServlet("/action")
public class ActionLoadServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      Query query = new Query("Actions").addSort("time", SortDirection.DESCENDING);
      PreparedQuery results = datastore.prepare(query);

      ArrayList<Action> actions = new ArrayList();
      for (Entity e : results.asIterable()) {
        String id = (String) e.getProperty("revisionId");
        String user = (String) e.getProperty("user");
        long time = (long) e.getProperty("time");
        String action = (String) e.getProperty("action");

        Action a = new Action(id, user, action, time);
        actions.add(a);
      }
      
      Gson gson = new Gson();

      response.setContentType("application/json");
      response.getWriter().println(gson.toJson(actions));
    }
}
