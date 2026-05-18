package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.LoginRequest;
import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.model.PasswordForgotRequest;
import com.ptithcm.documentshub.model.PasswordResetRequest;
import com.ptithcm.documentshub.model.RegisterCompleteRequest;
import com.ptithcm.documentshub.model.RegisterRequest;
import com.ptithcm.documentshub.model.RegisterVerifyRequest;
import com.ptithcm.documentshub.model.RegisterVerifyResponse;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.AuthService;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthRepository {
    private AuthService authService;

    public AuthRepository() {
        this.authService = ApiClient.createService(AuthService.class);
    }

    public void login(String identity, String password, Callback<ApiResponse<LoginResponse>> callback) {
        LoginRequest request = new LoginRequest(identity, password);
        authService.login(request).enqueue(callback);
    }

    public void forgotPassword(String identity, Callback<ApiResponse<Void>> callback) {
        PasswordForgotRequest request = new PasswordForgotRequest(identity);
        authService.forgotPassword(request).enqueue(callback);
    }

    public void resetPassword(String identity, String otpCode, String newPassword, Callback<ApiResponse<Void>> callback) {
        PasswordResetRequest request = new PasswordResetRequest(identity, otpCode, newPassword);
        authService.resetPassword(request).enqueue(callback);
    }

    public void registerRequest(String email, Callback<ApiResponse<Void>> callback) {
        RegisterRequest request = new RegisterRequest(email);
        authService.registerRequest(request).enqueue(callback);
    }

    public void registerVerify(String email, String otpCode, Callback<ApiResponse<RegisterVerifyResponse>> callback) {
        RegisterVerifyRequest request = new RegisterVerifyRequest(email, otpCode);
        authService.registerVerify(request).enqueue(callback);
    }

    public void registerComplete(String email, String registrationCode, String username, String password, Callback<ApiResponse<LoginResponse>> callback) {
        RegisterCompleteRequest request = new RegisterCompleteRequest(email, registrationCode, username, password);
        authService.registerComplete(request).enqueue(callback);
    }
}
