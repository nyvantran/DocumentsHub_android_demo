package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.CategoryService;
import com.ptithcm.documentshub.network.api.DocumentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DocumentRepository {
    private DocumentService documentService;
    private CategoryService categoryService;

    public DocumentRepository() {
        this.documentService = ApiClient.createService(DocumentService.class);
        this.categoryService = ApiClient.createService(CategoryService.class);
    }

    public void getCategories(Callback<ApiResponse<List<Category>>> callback) {
        categoryService.getCategories().enqueue(callback);
    }

    public void searchDocuments(String query, int page, int limit, Callback<ApiResponse<List<Document>>> callback) {
        documentService.searchDocuments(query, page, limit).enqueue(callback);
    }

    public void getDocumentDetail(String id, Callback<ApiResponse<Document>> callback) {
        documentService.getDocumentDetail(id).enqueue(callback);
    }
}
