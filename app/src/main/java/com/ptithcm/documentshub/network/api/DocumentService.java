package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DocumentService {
    @GET("api/v1/search")
    Call<ApiResponse<List<Document>>> searchDocuments(
            @Query("q") String query,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sort") String sort,
            @Query("category_ids") Integer categoryId
    );


    @GET("api/v1/documents/{id}")
    Call<ApiResponse<Document>> getDocumentDetail(@Path("id") String id);

    @GET("api/v1/documents/{id}/download")
    Call<ApiResponse<String>> getDownloadUrl(@Path("id") String id);
}
