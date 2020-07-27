package com.google.sps.data;

public final  class EditComment {
    public final long id;
    public final String userName;
    public final String editCommentText;
    public final long date;
    public final int toxicity;
    public final String parentArticle;
    public final String status;

    public EditComment(long id, String userName, String editCommentText,
    long date, int toxicity, String parentArticle, String status) {
        this.id = id;
        this.userName = userName;
        this.editCommentText = editCommentText;
        this.date = date;
        this.toxicity = toxicity;
        this.parentArticle = parentArticle;
        this.status = status;
    }
}