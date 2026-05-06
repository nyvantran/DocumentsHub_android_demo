package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

public class RegisterVerifyRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("otp_code")
    private String otpCode;

    public RegisterVerifyRequest(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
