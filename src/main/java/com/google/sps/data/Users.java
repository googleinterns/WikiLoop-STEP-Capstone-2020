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

  }
}
