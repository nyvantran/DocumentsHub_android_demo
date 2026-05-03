package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.ReportReason;
import com.ptithcm.documentshub.model.ReportRequest;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReportService {
    @GET("api/v1/reports/available_reasons")
    Call<ApiResponse<List<ReportReason>>> getAvailableReasons();

    @POST("api/v1/reports/documents/{document_id}")
    Call<ApiResponse<Void>> reportDocument(
            @Path("document_id") String documentId,
            @Body ReportRequest request
    );
}
