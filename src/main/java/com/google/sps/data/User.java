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

package com.google.sps.data;

import com.google.sps.data.EditComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
 
/* This is single user class */
public final class User{

  private final String id;
  private final String userName;
  private String avgToxicityScore;
  private ArrayList<EditComment> listEditComments;

  public User(String id, String userName, ArrayList<EditComment> listEditComments) {
    this.id = id;
    this.userName = userName;
    this.listEditComments=  (ArrayList<EditComment>) listEditComments.clone();

    double average = 0;
    int size = listEditComments.size();
    for (int i = 0; i < size; i++) {
        String toxicityScore = this.listEditComments.get(i).getToxicityScore();
        average += Double.parseDouble(toxicityScore);
    }

    this.avgToxicityScore = String.valueOf(average/size);
    if (size == 0) this.avgToxicityScore = "0.0";
  }

  public String getId() {
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
      String toxicityScore = newEditComment.getToxicityScore();
      double tempVar = Double.parseDouble(this.avgToxicityScore) * size;
      tempVar += Double.parseDouble(toxicityScore);
      this.avgToxicityScore = String.valueOf(tempVar/(size + 1));
      this.listEditComments.add(newEditComment);
  }

  @Override
  public boolean equals(Object object) {
    if ((object == null) || !(object instanceof User)) {
        return false;
    }

    if (this == object) return true;

    return fieldsEquality((User) object);
  }

  
  private boolean fieldsEquality(User other) {
    return (this.id.equals(other.getId())) && (this.userName.equals(other.getUserName()));
  }

}