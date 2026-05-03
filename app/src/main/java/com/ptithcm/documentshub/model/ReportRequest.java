package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class ReportRequest {
    @SerializedName("reason")
    private int reasonId;
    
    @SerializedName("desc")
    private String description;

    public ReportRequest(int reasonId, String description) {
        this.reasonId = reasonId;
        this.description = description;
    }
}
