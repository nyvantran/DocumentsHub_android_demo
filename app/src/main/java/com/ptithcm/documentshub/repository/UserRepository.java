package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.model.User;
import com.ptithcm.documentshub.model.UserProfileUpdateRequest;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.UserService;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository {
    private UserService userService;

    public UserRepository() {
        this.userService = ApiClient.createService(UserService.class);
    }

    public void getMyProfile(Callback<ApiResponse<User>> callback) {
        userService.getMyProfile().enqueue(callback);
    }

    public void updateMyProfile(UserProfileUpdateRequest request, Callback<ApiResponse<User>> callback) {
        userService.updateMyProfile(request).enqueue(callback);
    }

    public void updateAvatar(MultipartBody.Part avatar, Callback<ApiResponse<String>> callback) {
        userService.updateAvatar(avatar).enqueue(callback);
    }

    public void getLikedDocuments(Callback<ApiResponse<List<Document>>> callback) {
        userService.getLikedDocuments().enqueue(callback);
    }
}
