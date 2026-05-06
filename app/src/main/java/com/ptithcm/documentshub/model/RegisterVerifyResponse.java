package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class RegisterVerifyResponse {
    @SerializedName("registration_code")
    private String registrationCode;

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }
}
