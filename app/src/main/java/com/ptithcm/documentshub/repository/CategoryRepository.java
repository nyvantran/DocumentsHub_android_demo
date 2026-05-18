package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.CategoryService;

import java.util.List;

import retrofit2.Callback;

public class CategoryRepository {
    private CategoryService categoryService;

    public CategoryRepository() {
        this.categoryService = ApiClient.createService(CategoryService.class);
    }

    public void getCategories(Callback<ApiResponse<List<Category>>> callback) {
        categoryService.getCategories().enqueue(callback);
    }
}
