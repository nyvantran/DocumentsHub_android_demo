package com.ptithcm.documentshub.network.api;

import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.CollectionRequest;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface CollectionService {
    @GET("api/v1/users/me/collections")
    Call<ApiResponse<List<Collection>>> getMyCollections(@Query("page") int page, @Query("limit") int limit);

    @POST("api/v1/collections")
    Call<ApiResponse<Collection>> createCollection(@Body CollectionRequest request);

    @GET("api/v1/collections/{collection_id}/items")
    Call<ApiResponse<List<Document>>> getCollectionItems(@Path("collection_id") String collectionId);

    @PUT("api/v1/collections/{collection_id}/items/{document_id}")
    Call<ApiResponse<Void>> addItemToCollection(@Path("collection_id") String collectionId, @Path("document_id") String documentId);

    @DELETE("api/v1/collections/{collection_id}")
    Call<ApiResponse<Void>> deleteCollection(@Path("collection_id") String collectionId);

    @DELETE("api/v1/collections/{collection_id}/items/{document_id}")
    Call<ApiResponse<Void>> removeItemFromCollection(@Path("collection_id") String collectionId, @Path("document_id") String documentId);
}
