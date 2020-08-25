package com.google.sps.data;
import java.util.Date;
import java.util.Objects;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

/**
 * Class that implements the template for an edit comment. This is the 
 * main class used to store information of Wikipedia edit comments such 
 * as username, comment, data, article it came from, revisionid, and 
 * toxicity label (perspective api attribute : label, reason, score
 */
public final class EditComment {
  private final String userName;
  private final String comment;
  private final String date;
  private final String parentArticle;
  private final String status;
  private final String revisionId;
  private String toxicityObject;
  private String toxicityScore;
  private String looksGoodCounter;
  private String shouldReportCounter;
  private String notSureCounter;

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
  public EditComment(String revisionId, String userName, String comment, String toxicityObject, String date, String parentArticle, String status, String looksGoodCounter, String shouldReportCounter, String notSureCounter) {
    this.revisionId = revisionId;
    this.userName = userName;
    this.comment = comment;
    this.date = date;
    this.parentArticle = parentArticle;
    this.status = status;
    this.toxicityObject = toxicityObject;
    this.looksGoodCounter = looksGoodCounter;
    this.shouldReportCounter = shouldReportCounter;
    this.notSureCounter = notSureCounter;

    if ((toxicityObject == null) || (toxicityObject == "")) this.toxicityScore = "0.0";
    else {
      try {
          JSONObject computedAttribute = (JSONObject) new JSONParser().parse(toxicityObject); 
          //check if comment is in the timeFrame
          this.toxicityScore = computedAttribute.get("score").toString();

        } catch(Exception e) {
        System.out.println(e);
        }
    }
  }

  public void setToxicityObject(String toxicityObject) {
    this.toxicityObject = toxicityObject;
    if ((toxicityObject != null) && (toxicityObject != "")) {
      try {
          JSONObject computedAttribute = (JSONObject) new JSONParser().parse(toxicityObject); 
          //check if comment is in the timeFrame
          this.toxicityScore = computedAttribute.get("score").toString();

        } catch(Exception e) {
        System.out.println(e);
        }
    }
  }

  public String getUserName() {
    return this.userName;
  }

  public String getRevisionId() {
      return this.revisionId;
  }

  public String getDate() {
    return this.date;
  }

  public String getComment() {
    return this.comment;
  }

  public String getStatus() {
    return this.status;
  }

  public String getParentArticle() {
    return this.parentArticle;
  } 

  public String getToxicityObject() {
    return this.toxicityObject;
  }

  public String getToxicityScore() {
    return this.toxicityScore;
  }

  public String getLooksGoodCounter() {
    return this.looksGoodCounter;
  }

  public String getShouldReportCounter() {
    return this.shouldReportCounter;
  }

  public String getNotSureCounter() {
    return this.notSureCounter;
  }

  public void incrementLooksGoodCounter() {
      this.looksGoodCounter = String.valueOf(Integer.parseInt(this.looksGoodCounter) + 1);
  }

  public void incrementShouldReportCounter() {
    this.shouldReportCounter = String.valueOf(Integer.parseInt(this.shouldReportCounter) + 1);
  }

  public void incrementNotSureCounter() {
    this.notSureCounter = String.valueOf(Integer.parseInt(this.notSureCounter) + 1);
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
