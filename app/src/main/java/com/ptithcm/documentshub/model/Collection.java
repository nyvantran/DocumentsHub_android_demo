package com.ptithcm.documentshub.model;

public class Collection {
    private String id;
    private String name;
    private int total_items;


    public Collection(String id, String name, int itemCount) {
        this.id = id;
        this.name = name;
        this.total_items = itemCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotal_items() {
        return total_items;
    }

}
