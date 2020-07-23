/* This is the User class */
package com.google.sps.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
 
 
/** An individual comment. */
public final class User{

  public final long id;
  public final String userName;
  public final String avgToxicityScore;
  //public final ArrayList<EditComment> list_edit_comments;
  public final ArrayList<String> list_edit_comments;

  public User(long id, String userName, String avgToxicityScore, ArrayList<String> list_edit_comments) {
    this.id = id;
    this.userName = userName;
    this.avgToxicityScore = avgToxicityScore;
    this.list_edit_comments= list_edit_comments;
  }
}