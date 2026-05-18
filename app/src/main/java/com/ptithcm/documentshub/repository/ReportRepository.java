package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.ReportReason;
import com.ptithcm.documentshub.model.ReportRequest;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.ReportService;

import java.util.List;

import retrofit2.Callback;

public class ReportRepository {
    private ReportService reportService;

    public ReportRepository() {
        this.reportService = ApiClient.createService(ReportService.class);
    }

    public void getAvailableReasons(Callback<ApiResponse<List<ReportReason>>> callback) {
        reportService.getAvailableReasons().enqueue(callback);
    }

    public void reportDocument(String documentId, int reasonId, String description, Callback<ApiResponse<Void>> callback) {
        ReportRequest request = new ReportRequest(reasonId, description);
        reportService.reportDocument(documentId, request).enqueue(callback);
    }
}
