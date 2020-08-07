package com.google.sps.data;
import java.util.Date;

/** Class containing server statistics. */
public final class EditComment {
  
  private final String userName;
  public final String comment;
  private final String date;
  private final String parentArticle;
  private final String status;
  private final String revisionId;
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
