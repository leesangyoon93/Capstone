package com.example.leesangyoon.washerinhands;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 29.
 */

public class Article {

    private String id = null;
    private String author = null;
    private String content = null;
    private String title = null;
    private String date = null;
    private String commentCount = null;

    public void setDate(String date) {
        this.date = date;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    private Article() {}

    public String getDate() {
        return date;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private static class Singleton {
        private static final Article article = new Article();
    }

    public static Article getInstance () {
        Log.e("development","create singleton instance : Article");
        return Singleton.article;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void initArticle(){
        this.id="00a00a0aaa00aa000000a0a0";
        this.content="";
        this.title="";
        this.commentCount="0";
    }
}
