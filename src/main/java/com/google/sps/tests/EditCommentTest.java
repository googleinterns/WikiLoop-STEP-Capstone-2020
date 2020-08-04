/*
package com.google.sps.tests;

import static org.mockito.Mockito.*;
import com.google.gson.Gson;

import com.google.sps.servlets.RetrieveEditServlet;

import java.util.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import javax.servlet.http.*;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.MockComment;


@RunWith(JUnit4.class)
public final class EditCommentTest {
    //Test if data successfully retrieved from discover page
    //either test each attribute seperately or all in one
    @Test
    public void retrieveCorrectData() throws IOException{
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      ArrayList<EditComment> list = new ArrayList<EditComment>();
      EditComment edit1 = new EditComment(861223655, "K6ka",
      "Your explanation on the talk page is completely ludicrous.",
      "74%", "September 25, 2018 23:40", "https://en.wikipedia.org/w/index.php?title=Incivility",
      "None");
      EditComment edit2 = new EditComment(758943201, "Tom",
      "Your comment is pretty ignorant.","83.79%", "September, 5 2019, 12:40", 
      "https://en.wikipedia.org/w/index.php?title=Incivility", "None");

      EditComment edit3 = new EditComment(135138032, "Jerry", 
      "Your are the worst!","66.55%", "September, 6 2019, 21:09",
      "https://en.wikipedia.org/w/index.php?title=Incivility", "None");

      list.add(edit1);
      list.add(edit2);
      list.add(edit3);

      when(request.getParameter("revivionId")).thenReturn(861223655);

      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter();
      when(response.getWriter()).thenReturn(pw);


      Gson gson = new Gson();

      EditComment expected = new EditComment(861223655, "K6ka",
      "Your explanation on the talk page is completely ludicrous.",
      "74%", "September 25, 2018 23:40", "https://en.wikipedia.org/w/index.php?title=Incivility",
      "None");

      EditComment actual = new RetrieveEditServlet().doGet(request, response);
      
      Assert.assertEquals(expected, actual);

    }


}
*/