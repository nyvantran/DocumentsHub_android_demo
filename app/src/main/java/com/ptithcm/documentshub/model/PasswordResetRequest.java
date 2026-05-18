package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class PasswordResetRequest {
    @SerializedName("identity")
    private String identity;

    @SerializedName("otp_code")
    private String otpCode;

    @SerializedName("new_password")
    private String newPassword;

    public PasswordResetRequest(String identity, String otpCode, String newPassword) {
        this.identity = identity;
        this.otpCode = otpCode;
        this.newPassword = newPassword;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
