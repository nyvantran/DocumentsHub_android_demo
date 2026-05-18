package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class PasswordForgotRequest {
    @SerializedName("identity")
    private String identity;

    public PasswordForgotRequest(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
