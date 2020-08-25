package com.google.sps.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.google.sps.data.User;
import com.google.sps.data.EditComment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Class tests the UserProfileServlet logic
 */
@RunWith(JUnit4.class)
public final class UserProfileTest {
  // Tolerable error on the average toxicity score.
 // private static final double ACCEPTABLE_ERROR = 0.00001;

//  private static final EditComment ec1 = new EditComment("283416010", "Giano II", "/* Comments */ what the fuck use is that?",
 //   "91", "2009-04-12T19:57:15Z", "Wikipedia:Administrators' noticeboard/Incidents", "pending");
 // private static final EditComment ec2 = new EditComment("283242467", "Giano II", "/* Please... */ Oh do go away and take your nasty, little Admin habits with you. Be a man and do something useful with your time, have a chat with Neurolysis and accept another apology on my behalf, a",
 //   "68", "2009-04-11T20:58:39Z", "User talk:Giano II", "pending");
 /// private static final EditComment ec3 = new EditComment("290490251", "Giano II", "/* Discussion */ If she wants to chat on IRC - fine, but don't come here wanting to be an admin on the strength of it.", "32", "2009-05-17T11:41:52Z", "Wikipedia:Requests for adminship/FlyingToaster 2",
 //   "pending");
 // private static final EditComment ec4 = new EditComment("290692859", "Giano II", "/* Oppose */ accept it and stop badgering! ~~~~",
 //   "32", "2009-05-18T11:13:20Z", "Wikipedia:Requests for adminship/FlyingToaster 2", "pending");
 // private static final EditComment ec5 = new EditComment("290547577", "Giano II", "/* Time now for 2groups of editors each with their own Admins. */ That the chanel is for the banal is not in dispute, the problem is when the banal decide they are bored there, and having done little",
//    "12", "2009-05-17T17:46:35Z", "User talk:Giano II", "pending");

 // private static final String USER_KEY = "/wikipedia/en/User:Giano II";

 // private static final String USER_NAME = "Giano II";

  
  @Test
  public void InitializeListOfEditComments() {
    // Test that the User class correctly initializes the list of comments
    ArrayList<EditComment> expected = new ArrayList<EditComment>();
    
    //expected.add(ec1);
    //expected.add(ec2);
   // expected.add(ec3);
   // expected.add(ec4);
   // expected.add(ec5);

    //User user = new User(USER_KEY, USER_NAME, expected);

    //ArrayList<EditComment> actual = user.getListEditComments();
 //Assert.assertArrayEquals(expected.toArray(), actual.toArray());

  }
/*
  @Test
  public void UpdateListOfEditComments() {
    // Test that the User class correctly updates the list of comments when one more comment in added manually
    ArrayList<EditComment> expected = new ArrayList<EditComment>();

    expected.add(ec1);
    expected.add(ec2);

    User user = new User(USER_KEY, USER_NAME, expected);
    user.addEditComment(ec3);
    expected.add(ec3);

    ArrayList<EditComment> actual = user.getListEditComments();
    //Assert.assertArrayEquals(expected.toArray(), actual.toArray());

  }

  @Test
  public void CalculateAvgToxicityScoreOnInitialization() {
    // Test that the User class correctly calculates the average toxicity score on initialization
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    
    listEditComments.add(ec1);
    listEditComments.add(ec2);
    listEditComments.add(ec3);
    listEditComments.add(ec4);
    listEditComments.add(ec5);

    User user = new User(USER_KEY, USER_NAME, listEditComments);

    double actual = Double.parseDouble(user.getAvgToxicityScore());
    double expected = 0;

    int size = listEditComments.size();
    for (int i = 0; i < size; i++) {
        String toxicityObject = listEditComments.get(i).getToxicityObject();
        expected += Double.parseDouble(toxicityObject);
    }
    expected /= size;
   // Assert.assertEquals(expected, actual, ACCEPTABLE_ERROR);
  }

  @Test
  public void UpdateAvgToxicityScoreAfterAddedNewEditComment() {
    // Test that the User class correctly updates the average toxicity score when one more comment is added manually.
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();

    listEditComments.add(ec1);
    listEditComments.add(ec2);
    listEditComments.add(ec3);

    User user = new User(USER_KEY, USER_NAME, listEditComments);

    user.addEditComment(ec4);
    listEditComments.add(ec4);

    double actual = Double.parseDouble(user.getAvgToxicityScore());
    double expected = 0;

    int size = listEditComments.size();
    for (int i = 0; i < size; i++) {
        String toxicityObject = listEditComments.get(i).getToxicityObject();
        expected += Double.parseDouble(toxicityObject);
    }
    expected /= size;
    //Assert.assertEquals(expected, actual, ACCEPTABLE_ERROR);
  }

  @Test
  public void CalculateAvgToxicityScoreForNoEditComments() {
    // Test that the User class initializes the average toxicity score to 0.0 if the list of edit comments is empty.
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();

    User user = new User(USER_KEY, USER_NAME, listEditComments);
    double actual = Double.parseDouble(user.getAvgToxicityScore());
    double expected = 0.0;

    //Assert.assertEquals(expected, actual, ACCEPTABLE_ERROR);
  } */
}