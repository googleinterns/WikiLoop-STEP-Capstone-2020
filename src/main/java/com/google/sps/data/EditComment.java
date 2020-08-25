package com.google.sps.data;
import java.util.Date;
import java.util.Objects;

/**
 * Class that implements the template for an edit comment. This is the 
 * main class used to store information of Wikipedia edit comments such 
 * as username, comment, data, article it came from, revisionid, and 
 * toxicity label (perspective api attribute : label, reason, score
 */
public final class EditComment {
  public final String userName;
  public String comment;
  public final String date;
  public final String parentArticle;
  public final String status;
  public final String revisionId;
  public String toxicityObject;
  // Looks good
  //
  /**
   * Stores information regarding a Wikipedia edit comment
   * @param revisionId Wikipedia's revisionId from comment
   * @param userName Username of the person that made comment
   * @param comment text content of the edit comment
   * @param toxicObject string json of Attribute class that contains label, reason, and probability of label
   * @param date Date the comment was made
   * @param parentArticle Aritcle the comment was made in
   * @param status Status of the comment, was action taken upon the comoent
   */
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

  public String getRevisionId() {
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

  @Override
  public boolean equals(Object object) {
    if ((object == null) || !(object instanceof EditComment)) {
        return false;
    }

    if (this == object) return true;

    return fieldsEquality((EditComment) object);
  }

  private boolean fieldsEquality(EditComment other) {
    return (this.userName.equals(other.getUserName())) && (this.comment.equals(other.getComment())) && (this.date.equals(other.getDate())) && (this.parentArticle.equals(other.getParentArticle()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.userName, this.comment, this.date);
  }

}
