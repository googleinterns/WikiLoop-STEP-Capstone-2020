/* This is the User class */
package com.google.sps.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
 
 
/** An individual comment. */
/* TO DO (DEUS):   *//* TO DO (DEUS):   Use the actual EditComment Class as soon as David pushes some code*/
public final class User{

  public final long id;
  public final String userName;
  public final String avgToxicityScore;
  public final ArrayList<EditComment> listEditComments;
  //public final ArrayList<String> listEditComments;

  public User(long id, String userName, String avgToxicityScore, ArrayList<EditComment> listEditComments) {
    this.id = id;
    this.userName = userName;
    this.avgToxicityScore = avgToxicityScore;
    this.listEditComments= listEditComments;
  }
}