package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.LoginRequest;
import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.model.RefreshRequest;
import com.ptithcm.documentshub.network.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/v1/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/v1/auth/refresh")
    Call<ApiResponse<String>> refresh(@Body RefreshRequest request);

    @GET("api/v1/auth/whoami")
    Call<ApiResponse<Object>> whoami();
}
