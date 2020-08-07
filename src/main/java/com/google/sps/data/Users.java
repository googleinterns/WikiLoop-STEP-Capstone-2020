// Copyright 2019 Google LLC
//
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

package com.google.sps.data;
 
import com.google.sps.data.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public final class Users {
  private static final String EDIT_COMMENT_TOM_1 = "This is Tom's first comment";
  private static final String EDIT_COMMENT_TOM_2 = "This is Tom's second comment";
  private static final String EDIT_COMMENT_TOM_3 = "This is Tom's third comment";
  private static final String EDIT_COMMENT_TOM_4 = "This is Tom's fourth comment";
  private static final String EDIT_COMMENT_AMELIA_1 = "This is Amelia's first comment";
  private static final String EDIT_COMMENT_AMELIA_2 = "This is Amelia's second comment";
  private static final String EDIT_COMMENT_AMELIA_3 = "This is Amelia's third comment";
  private static final String EDIT_COMMENT_NICOLE_1 = "This is Nicole's first comment";
  private static final String EDIT_COMMENT_NICOLE_2 = "This is Nicole's second comment";
  private static final String EDIT_COMMENT_NICOLE_3 = "This is Nicole's third comment";


  private static ArrayList<String> EDIT_COMMENTS_TOM;
  

  private static ArrayList<String> EDIT_COMMENTS_AMELIA;
  

  private static ArrayList<String> EDIT_COMMENTS_NICOLE;
  

  public static ArrayList<User> users;

  public Users() {
    // Disallow instances.
    this.EDIT_COMMENTS_TOM = new ArrayList<String>();
    this.EDIT_COMMENTS_AMELIA = new ArrayList<String>();
    this.EDIT_COMMENTS_NICOLE = new ArrayList<String>();
    this.users = new ArrayList<User>();


    this.EDIT_COMMENTS_TOM.add(EDIT_COMMENT_TOM_1);
    this.EDIT_COMMENTS_TOM.add(EDIT_COMMENT_TOM_2);
    this.EDIT_COMMENTS_TOM.add(EDIT_COMMENT_TOM_3);
    this.EDIT_COMMENTS_TOM.add(EDIT_COMMENT_TOM_4);

    this.EDIT_COMMENTS_AMELIA.add(EDIT_COMMENT_AMELIA_1);
    this.EDIT_COMMENTS_AMELIA.add(EDIT_COMMENT_AMELIA_2);
    this.EDIT_COMMENTS_AMELIA.add(EDIT_COMMENT_AMELIA_3);

    this.EDIT_COMMENTS_NICOLE.add(EDIT_COMMENT_NICOLE_1);
    this.EDIT_COMMENTS_NICOLE.add(EDIT_COMMENT_NICOLE_2);
    this.EDIT_COMMENTS_NICOLE.add(EDIT_COMMENT_NICOLE_3);
 
    //this.users.add(new User(100, "Tom", "54%", this.EDIT_COMMENTS_TOM));
    //this.users.add(new User(101, "Amelia", "32%", this.EDIT_COMMENTS_AMELIA));
    //this.users.add(new User(102, "Nicole", "77%", this.EDIT_COMMENTS_NICOLE));

 /*
    Collection<EditComment> listEditComments = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment("82141", "Tom", "Your comment is pretty ignorant.","66.55", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment("13513", "Tom", "You are the worst!","83.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec3 = new EditComment("13514", "Tom", "You are the worst!","33.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec4 = new EditComment("13515", "Tom", "You are the worst!","23.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec5 = new EditComment("13516", "Tom", "You are the worst!","53.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec6 = new EditComment("13517", "Tom", "You are the worst!","43.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec7 = new EditComment("13518", "Tom", "You are the worst!","13.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec8 = new EditComment("13519", "Tom", "You are the worst!","63.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec9 = new EditComment("23513", "Tom", "You are the worst!","73.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec10 = new EditComment("33513", "Tom", "You are the worst!","93.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec11 = new EditComment("43513", "Tom", "You are the worst!","81.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec12 = new EditComment("53513", "Tom", "You are the worst!","82.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec13 = new EditComment("63513", "Tom", "You are the worst!","84.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec14 = new EditComment("73513", "Tom", "You are the worst!","85.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec15 = new EditComment("83513", "Tom", "You are the worst!","86.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec16 = new EditComment("14513", "Jean", "You are the worst!","87.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec17= new EditComment("15513", "Nelly", "You are the worst!","88.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec18 = new EditComment("16513", "Paul", "You are the worst!","89.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec19 = new EditComment("17513", "Nicole", "You are the worst!","90.79", "September, 6 2019, 21:09","incivility", "pending");
    EditComment ec20 = new EditComment("18513", "Nicole", "You are the worst!","93.79", "September, 6 2019, 21:09","incivility", "pending");
    listEditComments.add(ec1);
    listEditComments.add(ec2);
    listEditComments.add(ec3);
    listEditComments.add(ec4);
    listEditComments.add(ec5);
    listEditComments.add(ec6);
    listEditComments.add(ec7);
    listEditComments.add(ec8);
    listEditComments.add(ec9);
    listEditComments.add(ec10);
    listEditComments.add(ec11);
    listEditComments.add(ec12);
    listEditComments.add(ec13);
    listEditComments.add(ec14);
    listEditComments.add(ec15); */
  }
}
