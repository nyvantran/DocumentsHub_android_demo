package com.ptithcm.documentshub.model;

import java.util.List;

public class Document {
    private String id;
    private String title;
    private String file_thumbnail_url;
    private String file_type;
    private String visibility;
    private String status;
    private String owner;
    private int page_count;
    private int view_count;
    private int like_count;
    private int download_count;
    private String category;
    private List<String> tags;
    private String created_at;
    private String desc;
    private String file_preview_url;
    private boolean liked;


    // Constructor cũ để tương thích với code hiện tại
    public Document(String title, String author, String visibility, int pages, String category) {
        this.title = title;
        this.owner = author;
        this.visibility = visibility;
        this.page_count = pages;
        this.category = category;
    }

    // Constructor mới đầy đủ
    public Document(String id, String title, String author, String date, int views, int likes, int download_count, String fileUrl, String description) {
        this.id = id;
        this.title = title;
        this.owner = author;
        this.created_at = date;
        this.view_count = views;
        this.like_count = likes;
        this.download_count = download_count;
        this.file_thumbnail_url = fileUrl;
        this.desc = description;
    }

    public String getFile_type() {
        return file_type;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getFile_preview_url() {
        return file_preview_url;
    }

    public String getStatus() {
        return status;
    }

    public boolean getLiked() {
        return liked;
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getView_count() {
        return view_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public int getDownload_count() {
        return download_count;
    }

    public String getFile_thumbnail_url() {
        return file_thumbnail_url.replace("localhost", "10.0.2.2");
    }

    public String getDesc() {
        return desc;
    }

    public String getVisibility() {
        return visibility;
    }

    public int getPage_count() {
        return page_count;
    }

    public String getCategory() {
        return category;
    }
}