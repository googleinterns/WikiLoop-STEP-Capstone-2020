package com.google.sps.servlets;

import java.util.ArrayList;
import com.google.gson.Gson;

//datastore
import com.google.appengine.api.datastore.*;

//classes
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
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

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //retrieve edit information from discover page
      String id = request.getParameter("id");

      EditComment edit = retrieveEdit(id);

      Gson gson = new Gson();

      response.setContentType("application/json");
      response.getWriter().println(gson.toJson(edit));
    } 

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String revisionId = request.getParameter("revid");
      String user = request.getParameter("userIP");
      System.out.println("RES IP: " + user);
      String action = request.getParameter("btn");
      long time = System.currentTimeMillis();
      
      Entity statusEntity = new Entity("Actions");
      statusEntity.setProperty("revisionId", revisionId);
      statusEntity.setProperty("user", user);
      statusEntity.setProperty("time", time);
      statusEntity.setProperty("action", action);
      datastore.put(statusEntity);

      //update EditComments counters
      EditComment ec = retrieveEdit(revisionId);

      if (action.equals("g")) {
          System.out.println("g");
        ec.incrementLooksGoodCounter();
      } else if (action.equals("ns")) {
          System.out.println("ns");
        ec.incrementNotSureCounter();  
      } else if (action.equals("r")) {
          System.out.println("r");
        ec.incrementShouldReportCounter();
      }

      Entity edit = new Entity("EditComment", ec.getRevisionId() + "en");
      
      edit.setProperty("userName", ec.getUserName());
      edit.setProperty("comment", ec.getComment());
      edit.setProperty("date", ec.getDate());
      edit.setProperty("parentArticle", ec.getParentArticle());
      edit.setProperty("status", ec.getStatus());
      edit.setProperty("computedAttribute", ec.getToxicityObject());
      edit.setProperty("revisionId", ec.getRevisionId());
      edit.setProperty("toxicityScore", ec.getToxicityScore());
      edit.setProperty("looksGoodCounter", ec.getLooksGoodCounter());
      edit.setProperty("notSureCounter", ec.getNotSureCounter());
      edit.setProperty("shouldReportCounter", ec.getShouldReportCounter());
      datastore.put(edit);

      response.sendRedirect("/slider.html");
    }

    /* TO DO: Use Datastore */
    private EditComment retrieveEdit(String id) {
      // Filter query by the Key
      Key key = KeyFactory.createKey("EditComment", id + "en");
      Query query = new Query("EditComment").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
      //Query query = new Query("EditComment").setFilter(new Query.FilterPredicate("revisionId", Query.FilterOperator.EQUAL, id));
      PreparedQuery results = datastore.prepare(query);
      Entity entity = results.asSingleEntity();

      String userName = (String) entity.getProperty("userName");
      String comment = (String) entity.getProperty("comment");
      String date = (String) entity.getProperty("date");
      String parentArticle = (String) entity.getProperty("parentArticle");
      String status = (String) entity.getProperty("status");
      String toxicityObject = (String) entity.getProperty("computedAttribute");
      String revisionId = (String) entity.getProperty("revisionId");
      String gc = (String) entity.getProperty("looksGoodCounter");
      String nsc = (String) entity.getProperty("notSureCounter");
      String rc = (String) entity.getProperty("shouldReportCounter");


      EditComment ec = new EditComment(revisionId, userName, comment, toxicityObject, date, parentArticle, status, gc, rc, nsc);

      return ec;
    } 
}