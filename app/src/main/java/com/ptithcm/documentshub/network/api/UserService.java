package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.network.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("users/me/profile")
    Call<ApiResponse<Object>> getMyProfile();
}
