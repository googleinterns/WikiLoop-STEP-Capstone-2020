package com.google.sps.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.data.EditComment;
import com.google.sps.data.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.sps.servlets.UserProfileServlet;

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
import org.apache.commons.io.FileUtils;

import org.apache.commons.lang.builder.EqualsBuilder;


public class UserProfileTest {

    @Test
    public void returnValidAndCorrectJSONString() throws Exception {
        /*HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);    

        when(request.getParameter("ID")).thenReturn("me");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new UserProfileServlet().doGet(request, response);

        ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
        EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","66.55", "September, 5 2019, 12:40","incivility", "pending");
        EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","83.79", "September, 6 2019, 21:09","incivility", "pending");
        listEditComments.add(ec1);
        listEditComments.add(ec2);
        User expectedUser = new User((long)100, "Tom", listEditComments);
        Gson gson = new Gson();
        writer.flush();
        // Strip the last char which is a new line
        Assert.assertEquals(stringWriter.toString().substring(0,stringWriter.toString().length()-1), gson.toJson(expectedUser)); */
        
    }
}