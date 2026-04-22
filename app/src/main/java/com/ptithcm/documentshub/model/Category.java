package com.ptithcm.documentshub.model;

import java.util.List;

public class Category {
    private String name;
    private List<Document> trendingDocuments;

    public Category(String name, List<Document> trendingDocuments) {
        this.name = name;
        this.trendingDocuments = trendingDocuments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Document> getTrendingDocuments() {
        return trendingDocuments;
    }

    public void setTrendingDocuments(List<Document> trendingDocuments) {
        this.trendingDocuments = trendingDocuments;
    }
}