package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.model.DocumentUpdateRequest;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.DocumentService;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Callback;

public class DocumentRepository {
    private DocumentService documentService;

    public DocumentRepository() {
        this.documentService = ApiClient.createService(DocumentService.class);
    }

    /**
     * Upload tài liệu mới lên server qua multipart/form-data.
     *
     * @param body     MultipartBody chứa file và metadata
     * @param callback Callback xử lý kết quả
     */
    public void uploadDocument(MultipartBody body, Callback<ApiResponse<Void>> callback) {
        documentService.uploadDocument(body).enqueue(callback);
    }

    /**
     * Cập nhật thông tin tài liệu.
     *
     * @param id       ID tài liệu
     * @param request  Thông tin cần cập nhật
     * @param callback Callback xử lý kết quả
     */
    public void updateDocument(String id, DocumentUpdateRequest request, Callback<ApiResponse<Void>> callback) {
        documentService.updateDocument(id, request).enqueue(callback);
    }

    public void searchDocumentsByQuery(String query, Integer categoryId, int page, int limit, String sort, Callback<ApiResponse<List<Document>>> callback) {
        documentService.searchDocuments(query, page, limit, sort, categoryId).enqueue(callback);
    }


    public void getDocumentDetail(String id, Callback<ApiResponse<Document>> callback) {
        documentService.getDocumentDetail(id).enqueue(callback);
    }

    public void getDownloadUrl(String id, Callback<ApiResponse<String>> callback) {
        documentService.getDownloadUrl(id).enqueue(callback);
    }

    public void likeDocument(String id, Callback<ApiResponse<Void>> callback) {
        documentService.likeDocument(id).enqueue(callback);
    }

    public void unlikeDocument(String id, Callback<ApiResponse<Void>> callback) {
        documentService.unlikeDocument(id).enqueue(callback);
    }

    public void restoreDocument(String id, Callback<ApiResponse<Void>> callback) {
        documentService.restoreDocument(id).enqueue(callback);
    }

    public void getDeletedDocuments(int limit, Callback<ApiResponse<List<Document>>> callback) {
        documentService.getMyDocuments(limit, "DELETED").enqueue(callback);
    }

    public void getMyDocuments(int limit, String status, Callback<ApiResponse<List<Document>>> callback) {
        documentService.getMyDocuments(limit, status).enqueue(callback);
    }

    /**
     * Xóa mềm tài liệu (chuyển vào thùng rác).
     *
     * @param id       ID tài liệu
     * @param callback Callback xử lý kết quả
     */
    public void deleteDocument(String id, Callback<ApiResponse<Void>> callback) {
        documentService.deleteDocument(id).enqueue(callback);
    }
}
