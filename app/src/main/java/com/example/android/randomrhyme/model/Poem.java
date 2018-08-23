package com.example.android.randomrhyme.model;

public class Poem {

    private String author;
    private String poemTitle;
    private String poemBodyText;

    public Poem(String poemTitle, String poemBodyText, String author) {
        this.poemTitle = poemTitle;
        this.poemBodyText = poemBodyText;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPoemTitle() {
        return poemTitle;
    }

    public void setPoemTitle(String poemTitle) {
        this.poemTitle = poemTitle;
    }

    public String getPoemBodyText() {
        return poemBodyText;
    }

    public void setPoemBodyText(String poemBodyText) {
        this.poemBodyText = poemBodyText;
    }
}
