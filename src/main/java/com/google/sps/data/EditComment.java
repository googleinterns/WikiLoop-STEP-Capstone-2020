package com.google.sps.data;
import java.util.Date;

/** Class containing server statistics. */
public final class EditComment {
  
  public final String userName;
  public final String comment;
  public final String date;
  public final String parentArticle;
  public final String status;
  public final String revisionId;
  public String toxicityObject;

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
