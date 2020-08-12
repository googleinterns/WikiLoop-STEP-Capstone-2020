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
        return request.getParameter("id");
    }

    /* TO DO: Use Datastore */
    private EditComment retrieveEdit(String id) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      // Filter query by the Key
      Key key = KeyFactory.createKey("EditComment", id + "en");
      Query query = new Query("EditComment").setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key));
      PreparedQuery results = datastore.prepare(query);
      Entity entity = results.asSingleEntity();

      String userName = (String) entity.getProperty("userName");
      String comment = (String) entity.getProperty("comment");
      String date = (String) entity.getProperty("date");
      String parentArticle = (String) entity.getProperty("parentArticle");
      String status = (String) entity.getProperty("status");
      String toxicityObject = (String) entity.getProperty("computedAttribute");
      String revisionId = (String) entity.getProperty("revisionId");

      EditComment ec = new EditComment(revisionId, userName, comment, toxicityObject, date, parentArticle, status);

      return ec;
    }   
}