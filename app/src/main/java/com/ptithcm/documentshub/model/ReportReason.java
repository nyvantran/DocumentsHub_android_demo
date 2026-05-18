package com.ptithcm.documentshub.model;

//import com.google.gson.annotations.SerializedName;

public class ReportReason {
    private int id;
    private String code;
    private String description;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }
}
