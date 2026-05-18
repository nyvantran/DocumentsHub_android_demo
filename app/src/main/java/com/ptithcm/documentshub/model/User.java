package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;
import com.ptithcm.documentshub.network.ApiClient;

public class User {
    private static final String BASE_URL = ApiClient.getBaseUrl();

    @SerializedName("username")
    private String username;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("bio")
    private String bio;

    public String getUsername() {
        return username != null ? username : "n/a";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        if (avatarUrl == null) return null;

        return this.avatarUrl.replace("localhost", BASE_URL);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName != null ? fullName : "n/a";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender != null ? gender : "n/a";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "n/a";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio != null ? bio : "n/a";
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
