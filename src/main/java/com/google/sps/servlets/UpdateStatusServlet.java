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

@WebServlet("/update-status")
public class UpdateStatusServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      long id;
      String userName = request.getParameter("username");
      String editCommentText = request.getParameter("edit-comment");
      long date = request.getParameter("date");
      int toxicity = request.getParameter("toxic-score");
      String parentArticle = request.getParameter("article");
      String status;

      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("body", body);
      commentEntity.setProperty("timestamp", timestamp);
      datastore.put(commentEntity);

      response.sendRedirect("/comments.html");
        
    }
}