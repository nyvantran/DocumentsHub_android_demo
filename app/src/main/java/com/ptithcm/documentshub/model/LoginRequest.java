package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("identity")
    private String identity;

    @SerializedName("password")
    private String password;

    public LoginRequest(String identity, String password) {
        this.identity = identity;
        this.password = password;
    }
}
