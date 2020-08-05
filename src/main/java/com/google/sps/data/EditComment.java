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
import java.util.Date;

/** Class containing server statistics. */
public final class EditComment {
  
  private final String userName;
  private final String comment;
  private final String date;
  private final String parentArticle;
  private final String status;
  private final String revisionId;
  private final String toxicityObject;

  public EditComment(String revisionId, String userName, String comment, String toxicityObject, String date, String parentArticle, String status) {
    this.revisionId = revisionId;
    this.userName = userName;
    this.comment = comment;
    this.date = date;
    this.parentArticle = parentArticle;
    this.status = status;
    this.toxicityObject = toxicityObject;
  }

  public String getUserName() {
    return userName;
  }

  public Integer getRevisionId() {
      return revisionId;
  }

  public String getDate() {
    return date;
  }

  public String getComment() {
    return comment;
  }

  public String getStatus() {
    return status;
  }

  public String getParentArticle() {
    return parentArticle;
  }

  public String getToxicityObject() {
    return toxicityObject;
  }

}
