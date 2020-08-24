package com.google.sps.tests;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.FileReader; 
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

/** 
 * Class for loading testing data gto datastore
 */
public final class TestData {
  public TestData() {

  }

  private void loadComments() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    ArrayList<EditComment> listEditComments = new ArrayList<EditComment>();
    //listEditComments.add(new EditComment())
    for (EditComment editComment: listEditComments) {
      Entity editCommentEntity = new Entity("TestResponses", editComment.getRevisionId() + "en");
      editCommentEntity.setProperty("revisionId", editComment.getRevisionId());
      editCommentEntity.setProperty("userName", editComment.getUserName());
      editCommentEntity.setProperty("comment", editComment.getComment());
      editCommentEntity.setProperty("computedAttribute", editComment.getToxicityObject());
      editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
      editCommentEntity.setProperty("date", editComment.getDate());
      editCommentEntity.setProperty("status", editComment.getStatus());
      datastore.put(editCommentEntity);
    }
  }
}