package com.ptithcm.documentshub.model;

public class Collection {
    private String id;
    private String name;
    private int itemCount;
    private String thumbnailUrl;

    public Collection(String id, String name, int itemCount, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.itemCount = itemCount;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
