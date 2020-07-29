package com.google.sps.data;

import com.google.sps.data.EditComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
 
/* This is single user class */
public final class User{

  private final long id;
  private final String userName;
  private String avgToxicityScore;
  private ArrayList<EditComment> listEditComments;

  public User(long id, String userName, ArrayList<EditComment> listEditComments) {
    this.id = id;
    this.userName = userName;
    this.listEditComments= listEditComments;

    double average = 0;
    int size = listEditComments.size();
    for (int i = 0; i < size; i++) {
        average += Double.parseDouble(this.listEditComments.get(i).getToxicityObject());
    }

    this.avgToxicityScore = String.valueOf(average/size);
  }

  public long getID() {
      return this.id;
  }

  public String getUserName() {
      return this.userName;
  }

  public String getAvgToxicityScore() {
      return this.avgToxicityScore;
  }

  public ArrayList<EditComment> getListEditComments() {
      return this.listEditComments;
  }

  public void addEditComment(EditComment newEditComment) {
      int size = this.listEditComments.size();
      double tempVar = Double.parseDouble(this.avgToxicityScore) * size;
      tempVar += Double.parseDouble(newEditComment.getToxicityObject());
      this.avgToxicityScore = String.valueOf(tempVar/(size + 1));
      this.listEditComments.add(newEditComment);
  }

}