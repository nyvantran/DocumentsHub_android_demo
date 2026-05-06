package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.LoginRequest;
import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.model.PasswordForgotRequest;
import com.ptithcm.documentshub.model.PasswordResetRequest;
import com.ptithcm.documentshub.model.RegisterCompleteRequest;
import com.ptithcm.documentshub.model.RegisterRequest;
import com.ptithcm.documentshub.model.RegisterVerifyRequest;
import com.ptithcm.documentshub.model.RegisterVerifyResponse;
import com.ptithcm.documentshub.model.RefreshRequest;
import com.ptithcm.documentshub.network.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/v1/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/v1/auth/refresh")
    Call<ApiResponse<String>> refresh(@Body RefreshRequest request);

    @GET("api/v1/auth/whoami")
    Call<ApiResponse<Object>> whoami();

    @POST("api/v1/auth/forgot_password")
    Call<ApiResponse<Void>> forgotPassword(@Body PasswordForgotRequest request);

    @POST("api/v1/auth/reset_password")
    Call<ApiResponse<Void>> resetPassword(@Body PasswordResetRequest request);

    @POST("api/v1/auth/register/request")
    Call<ApiResponse<Void>> registerRequest(@Body RegisterRequest request);

    @POST("api/v1/auth/register/verify")
    Call<ApiResponse<RegisterVerifyResponse>> registerVerify(@Body RegisterVerifyRequest request);

    @POST("api/v1/auth/register/complete")
    Call<ApiResponse<LoginResponse>> registerComplete(@Body RegisterCompleteRequest request);
}
