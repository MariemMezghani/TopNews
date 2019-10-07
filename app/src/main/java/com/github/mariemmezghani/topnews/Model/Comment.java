package com.github.mariemmezghani.topnews.Model;

public class Comment {
    private String name;
    private String text;
    private String newsTitle;

    public Comment(){

    }

    public Comment(String name, String text, String newsTitle) {
        this.name = name;
        this.text = text;
        this.newsTitle=newsTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
}
