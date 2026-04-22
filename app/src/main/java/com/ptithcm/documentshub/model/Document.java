package com.ptithcm.documentshub.model;

public class Document {
    private String title;
    private String author;
    private String visibility;
    private int pages;
    private String category;

    public Document(String title, String author, String visibility, int pages, String category) {
        this.title = title;
        this.author = author;
        this.visibility = visibility;
        this.pages = pages;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getVisibility() {
        return visibility;
    }

    public int getPages() {
        return pages;
    }

    public String getCategory() {
        return category;
    }
}