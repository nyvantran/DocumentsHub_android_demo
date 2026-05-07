package com.ptithcm.documentshub.repository;

import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.CollectionRequest;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.CollectionService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CollectionRepository {
    private CollectionService collectionService;

    public CollectionRepository() {
        collectionService = ApiClient.createService(CollectionService.class);
    }

    public void getMyCollections(int page, int limit, Callback<ApiResponse<List<Collection>>> callback) {
        Call<ApiResponse<List<Collection>>> call = collectionService.getMyCollections(page, limit);
        call.enqueue(callback);
    }

    public void createCollection(String name, Callback<ApiResponse<Collection>> callback) {
        CollectionRequest request = new CollectionRequest(name);
        collectionService.createCollection(request).enqueue(callback);
    }

    public void getCollectionItems(String collectionId, Callback<ApiResponse<List<Document>>> callback) {
        collectionService.getCollectionItems(collectionId).enqueue(callback);
    }

    public void addItemToCollection(String collectionId, String documentId, Callback<ApiResponse<Void>> callback) {
        collectionService.addItemToCollection(collectionId, documentId).enqueue(callback);
    }

    public void deleteCollection(String collectionId, Callback<ApiResponse<Void>> callback) {
        collectionService.deleteCollection(collectionId).enqueue(callback);
    }

    public void removeItemFromCollection(String collectionId, String documentId, Callback<ApiResponse<Void>> callback) {
        collectionService.removeItemFromCollection(collectionId, documentId).enqueue(callback);
    }
}
