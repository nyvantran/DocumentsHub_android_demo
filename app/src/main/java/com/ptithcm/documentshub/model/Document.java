package com.ptithcm.documentshub.model;

public class Document {
    private String id;
    private String title;
    private String author;
    private String date;
    private int views;
    private int likes;
    private int downloads;
    private String fileUrl;
    private String description;
    private String visibility;
    private int pages;
    private String category;

    // Constructor cũ để tương thích với code hiện tại
    public Document(String title, String author, String visibility, int pages, String category) {
        this.title = title;
        this.author = author;
        this.visibility = visibility;
        this.pages = pages;
        this.category = category;
    }

    // Constructor mới đầy đủ
    public Document(String id, String title, String author, String date, int views, int likes, int downloads, String fileUrl, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.views = views;
        this.likes = likes;
        this.downloads = downloads;
        this.fileUrl = fileUrl;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }
    public int getDownloads() { return downloads; }
    public String getFileUrl() { return fileUrl; }
    public String getDescription() { return description; }
    public String getVisibility() { return visibility; }
    public int getPages() { return pages; }
    public String getCategory() { return category; }
}