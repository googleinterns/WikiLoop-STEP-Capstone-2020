package com.google.sps.data;
import java.util.Date;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.Comparator;

/** 
 * Takes response from edit comment page to store action
 */

public class Status {
  public Integer good;
  public Integer notSure;
  public Integer report;

  public Status(Integer good, Integer notSure, Integer report) {
      this.good = 0;
      this.notSure = 0;
      this.report = 0;
  }

  public Integer getGood() {
      return good;
  }

  public Integer getNotSure() {
      return notSure;
  }

  public Integer getReport() {
      return report;
  }

  public String toString() {
      return "Good: " + Integer.toString(good) + " " + "Not Sure: " + Integer.toString(notSure) + " " + "Report: " + Integer.toString(report);
  }
}