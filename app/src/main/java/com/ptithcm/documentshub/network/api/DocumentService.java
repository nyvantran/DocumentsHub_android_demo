package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.model.DocumentUpdateRequest;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DocumentService {

    /**
     * Upload tài liệu mới lên server.
     * Sử dụng @Body MultipartBody để kiểm soát hoàn toàn các parts,
     * tránh vấn đề Retrofit không gửi đúng list fields và nullable fields.
     */
    @POST("api/v1/documents")
    Call<ApiResponse<Void>> uploadDocument(@Body MultipartBody body);

    /**
     * Cập nhật thông tin tài liệu.
     * Endpoint: PATCH /api/v1/documents/{id}
     * Chỉ gửi các field cần thay đổi trong JSON body.
     *
     * @param id   ID tài liệu
     * @param body Thông tin cần cập nhật
     * @return ApiResponse
     */
    @PATCH("api/v1/documents/{id}")
    Call<ApiResponse<Void>> updateDocument(
            @Path("id") String id,
            @Body DocumentUpdateRequest body
    );

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

    @PUT("api/v1/documents/{id}/like")
    Call<ApiResponse<Void>> likeDocument(@Path("id") String id);

    @DELETE("api/v1/documents/{id}/like")
    Call<ApiResponse<Void>> unlikeDocument(@Path("id") String id);

    @POST("api/v1/documents/{id}/restore")
    Call<ApiResponse<Void>> restoreDocument(@Path("id") String id);

    @GET("api/v1/users/me/documents")
    Call<ApiResponse<List<Document>>> getMyDocuments(
            @Query("limit") int limit,
            @Query("statuses") String statuses
    );

    /**
     * Xóa mềm tài liệu (chuyển vào thùng rác).
     * Endpoint: DELETE /api/v1/documents/{id}
     *
     * @param id ID tài liệu cần xóa
     * @return ApiResponse
     */
    @DELETE("api/v1/documents/{id}")
    Call<ApiResponse<Void>> deleteDocument(@Path("id") String id);
}
