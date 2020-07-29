// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

/** */
@RunWith(JUnit4.class)
public final class UserTest {
  // Some people that we can use in our tests.
  private static final double ACCEPTABLE_ERROR = 0.00001;

  @Test
  public void InitializeListOfEditComments() {
    ArrayList<EditComment> expected = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","79.5", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","20.5", "September, 6 2019, 21:09","incivility", "pending");
    expected.add(ec1);
    expected.add(ec2);

    User user = new User((long)100, "Tom", expected);

    ArrayList<EditComment> actual = user.getListEditComments();
    Assert.assertArrayEquals(expected.toArray(), actual.toArray());

  }

  @Test
  public void UpdateListOfEditComments() {
    ArrayList<EditComment> expected = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","79.5", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","20.5", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec3 = new EditComment(15513, "Tom", "worst!","5", "September, 6 2019, 21:09","incivility", "pending");

    expected.add(ec1);
    expected.add(ec2);

    User user = new User((long)100, "Tom", expected);
    user.addEditComment(ec3);
    expected.add(ec3);

    ArrayList<EditComment> actual = user.getListEditComments();
    Assert.assertArrayEquals(expected.toArray(), actual.toArray());

  }

  @Test
  public void CalculateAvgToxicityScoreOnInitialization() {

    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","79.5", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","20.5", "September, 6 2019, 21:09","incivility", "pending");
    listEditComments.add(ec1);
    listEditComments.add(ec2);

    User user = new User((long)100, "Tom", listEditComments);

    double actual = Double.parseDouble(user.getAvgToxicityScore());
    double expected = 50.0;
    System.out.println(actual + "\t" + expected);
    Assert.assertEquals(expected, actual, ACCEPTABLE_ERROR);
  }

  @Test
  public void UpdateAvgToxicityScoreAfterAddedNewEditComment() {

    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment(82141, "Tom", "Your comment is pretty ignorant.","79.5", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment(13513, "Tom", "Your are the worst!","20.5", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec3 = new EditComment(15513, "Tom", "worst!","5", "September, 6 2019, 21:09","incivility", "pending");
    listEditComments.add(ec1);
    listEditComments.add(ec2);

    User user = new User((long)100, "Tom", listEditComments);
    user.addEditComment(ec3);

    double actual = Double.parseDouble(user.getAvgToxicityScore());
    double expected = 35.0;
    System.out.println(actual + "\t" + expected);
    Assert.assertEquals(expected, actual, ACCEPTABLE_ERROR);
  }
}