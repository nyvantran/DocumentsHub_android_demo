package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.LoginRequest;
import com.ptithcm.documentshub.model.LoginResponse;
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
}
