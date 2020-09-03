package com.google.sps.data;
import java.util.Date;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.Comparator;

/** 
 * Takes response from edit comment page to store action
 */

public class Action {
  public String id;
  public String user;
  public String action;
  public long time;
  public String randName;
  public Action(String id, String user, String action, long time, String randName) {
      this.id = id;
      this.user = user;
      this.action = action;
      this.time = time;
      this.randName = randName;
  }
}