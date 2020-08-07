
package com.google.sps.data;
import java.util.Date;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.Comparator;

/** 
 * Takes the response of the Perspective API to store and determine the
 * classifcation of the text on different attributes of TOXICITY, IDENTITY_ATTACK,
 * INSULT, PROFANITY, THREAT, SEXUALLY_EXPLICIT, FLIRTATION.
 * All attributes except for TOXICITY are experimental, thus will make mistakes and not 
 * tested for issues including user bias. When experimental attributes go into production, the code must be changed
 * Refer to this doc to learn more https://github.com/conversationai/perspectiveapi/blob/master/2-api/models.md
 */
public class Attribute {
  public String label;
  public String score;
  public String reason;
  public boolean experimental;

  public Attribute(String label, String score, String reason, boolean experimental) {
    this.label = label;
    this.score = score;
    this.reason = reason;
    this.experimental = experimental;
  }

   /**
   * A comparator for attribute by the highest probability
   */
  public static final Comparator<Attribute> ORDER_BY_HIGH = new Comparator<Attribute>() {
    @Override
    public int compare(Attribute a, Attribute b) {
      return Integer.compare(Integer.valueOf(b.score), Integer.valueOf(a.score));
    }
  };

  public String toString() {
   

    return "Label: "  + label + " " + "Reason: " + reason + " "  + "Probability: " + score + " "  + "Experimental: " + String.valueOf(experimental);
  }
}
