package com.google.sps.servlets;

import java.util.ArrayList;
import com.google.gson.Gson;

//classes
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.MockComment;

//servlet
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//get comment based on what is clicked in discover page
@WebServlet("/retrieve")
public class RetrieveEditServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //retrieve edit information from discover page

      //EditComment edit = request.getParameter("edit", edit); //change

      Gson gson = new Gson();

      response.setContentType("application/json");
      //response.getWriter().println(gson.toJson(edit));
    }        
}