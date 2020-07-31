package com.google.sps.tests;

import static org.mockito.Mockito.*;

import java.util.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.MockComment;


@RunWith(JUnit4.class)
public final class EditCommentTest {
    EditComment edit = new EditComment(861223655, "K6ka",
    "Your explanation on the talk page is completely ludicrous",
    "74%", "September 25, 2018 23:40", "https://en.wikipedia.org/w/index.php?title=Incivility",
    "None");

    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @Before
    public void setUp() {
      request = mock(HttpServletRequest.class);
      response = mock(HttpServletResponse.class);
        
    }
    //Test if data successfully retrieved from discover page
    //either test each attribute seperately or all in one
    @Test
    public void testServlet() throws IOException{
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      when(request.getParameter);
    }


}