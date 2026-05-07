package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class CollectionRequest {
    @SerializedName("name")
    private String name;

    public CollectionRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
