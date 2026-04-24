package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("api/v1/categories")
    Call<ApiResponse<List<Category>>> getCategories();
}
