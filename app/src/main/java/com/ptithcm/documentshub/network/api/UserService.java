package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.model.User;
import com.ptithcm.documentshub.model.UserProfileUpdateRequest;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserService {
    @GET("api/v1/users/me/profile")
    Call<ApiResponse<User>> getMyProfile();

    @PATCH("api/v1/users/me/profile")
    Call<ApiResponse<User>> updateMyProfile(@Body UserProfileUpdateRequest request);

    @Multipart
    @PUT("api/v1/users/me/avatar")
    Call<ApiResponse<String>> updateAvatar(@Part MultipartBody.Part avatar);

    @GET("api/v1/users/me/liked_documents")
    Call<ApiResponse<List<Document>>> getLikedDocuments();
}
