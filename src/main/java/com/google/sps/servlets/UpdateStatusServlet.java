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
import com.google.sps.data.Status;

//servlet
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Gson
import com.google.gson.Gson;

@WebServlet("/actions")
public class UpdateStatusServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Actions").addSort("time", SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(query);

        ArrayList<Status> editStatus = new ArrayList();
        for (Entity entity : pq.asIterable()) {
            String id = (String) entity.getProperty("revisionId");
            String user = (String) entity.getProperty("user");
            String action = (String) entity.getProperty("action");
            long time = (long) entity.getProperty("time");
            Status status = new Status(id, user, action, time);

            editStatus.add(status);
        }
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(editStatus));
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String revisionId = request.getParameter("Id");
      System.out.println("id: " + request.getParameter("Id"));
      //check what user is logged in
      String user = "Giano II";
      String action = getAction(request);
      long time = System.currentTimeMillis();
      
      Entity statusEntity = new Entity("Actions");
      statusEntity.setProperty("revisionId", revisionId);
      statusEntity.setProperty("user", user);
      statusEntity.setProperty("time", time);
      statusEntity.setProperty("action", action);
      datastore.put(statusEntity);

      response.sendRedirect("/actions.html");
      
    }

    private String getAction(HttpServletRequest request) {
        String action;
        if (request.getParameter("good") != null) {
            action = "Looks good";
        } else if (request.getParameter("notsure") != null) {
            action = "Not sure";
        } else if (request.getParameter("report") != null) {
            action = "Should report";
        } else {
            action = "N/A";
        }
        return action;
    }
}