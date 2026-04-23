package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.LoginRequest;
import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.network.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/v1/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);
}
