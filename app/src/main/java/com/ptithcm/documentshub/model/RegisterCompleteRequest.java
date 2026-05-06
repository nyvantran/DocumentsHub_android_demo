package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class RegisterCompleteRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("registration_code")
    private String registrationCode;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public RegisterCompleteRequest(String email, String registrationCode, String username, String password) {
        this.email = email;
        this.registrationCode = registrationCode;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
