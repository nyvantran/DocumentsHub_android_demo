package com.ptithcm.documentshub.viewmodel;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.CollectionRepository;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentViewModel extends ViewModel {
    private DocumentRepository repository;
    private CollectionRepository collectionRepository;
    private MutableLiveData<Document> document = new MutableLiveData<>();
    private MutableLiveData<List<Document>> similarDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Collection>> myCollections = new MutableLiveData<>();
    private MutableLiveData<String> downloadUrl = new MutableLiveData<>();
    private MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public DocumentViewModel() {
        repository = new DocumentRepository();
        collectionRepository = new CollectionRepository();
    }

    public LiveData<Document> getDocument() {
        return document;
    }

    public LiveData<String> getDownloadUrl() {
        return downloadUrl;
    }

    public LiveData<List<Document>> getSimilarDocuments() {
        return similarDocuments;
    }

    public LiveData<List<Collection>> getMyCollections() {
        return myCollections;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public void fetchMyCollections() {
        collectionRepository.getMyCollections(1, 20, new Callback<ApiResponse<List<Collection>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Collection>>> call, Response<ApiResponse<List<Collection>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myCollections.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Failed to fetch collections");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Collection>>> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }

    public void addItemToCollection(String collectionId) {
        Document currentDoc = document.getValue();
        if (currentDoc == null) return;

        collectionRepository.addItemToCollection(collectionId, currentDoc.getId(), new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    statusMessage.setValue("Đã lưu vào bộ sưu tập");
                } else {
                    errorMessage.setValue("Không thể lưu vào bộ sưu tập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }

    public void fetchDownloadUrl(String id) {
        android.util.Log.d("DocumentViewModel", "Fetching download URL for ID: " + id);
        repository.getDownloadUrl(id, new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String url = response.body().getData();
                    android.util.Log.d("DocumentViewModel", "Download URL received: " + url);
                    downloadUrl.setValue(url);
                } else {
                    android.util.Log.e("DocumentViewModel", "Failed to get download URL: " + (response.body() != null ? response.body().getMessage() : "Unknown error"));
                    errorMessage.setValue("Failed to get download URL");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                android.util.Log.e("DocumentViewModel", "Network error fetching download URL", t);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void clearDownloadUrl() {
        downloadUrl.setValue(null);
    }

    public void toggleLike() {
        Document currentDoc = document.getValue();
        if (currentDoc == null) return;

        boolean isLiked = currentDoc.getLiked();
        String docId = currentDoc.getId();

        Callback<ApiResponse<Void>> callback = new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    // Update local state
                    currentDoc.setLiked(!isLiked);
                    currentDoc.setLike_count(isLiked ? currentDoc.getLike_count() - 1 : currentDoc.getLike_count() + 1);
                    document.setValue(currentDoc);
                } else {
                    errorMessage.setValue("Failed to update like status");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        };

        if (isLiked) {
            repository.unlikeDocument(docId, callback);
        } else {
            repository.likeDocument(docId, callback);
        }
    }

    public void fetchDocumentDetail(String id) {
        repository.getDocumentDetail(id, new Callback<ApiResponse<Document>>() {
            @Override
            public void onResponse(Call<ApiResponse<Document>> call, Response<ApiResponse<Document>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    document.setValue(response.body().getData());
                    fetchSimilarDocuments(response.body().getData());
                } else {
                    loadDummyDocument(id);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Document>> call, Throwable t) {
                loadDummyDocument(id);
            }
        });
    }

    private void fetchSimilarDocuments(Document doc) {
        // Placeholder for similar documents logic
        loadDummySimilar();
    }

    private void loadDummyDocument(String id) {
        Document dummy = new Document(id, "Lập trình Android với Java", "Sơn Tùng M-TP", "20/04/2024", 1500, 450, 89, "",
                "Tài liệu hướng dẫn chi tiết về lập trình Android sử dụng ngôn ngữ Java từ cơ bản đến nâng cao. Nội dung bao gồm Activity, Fragment, Intent, RecyclerView và kiến trúc MVVM.");
        document.setValue(dummy);
        loadDummySimilar();
    }

    private void loadDummySimilar() {
        List<Document> similar = new ArrayList<>();
        similar.add(new Document("101", "Cẩm nang Java Web", "Admin", "01/01/2024", 500, 100, 20, "", ""));
        similar.add(new Document("102", "Spring Boot Pro", "Java Dev", "15/02/2024", 800, 250, 45, "", ""));
        similar.add(new Document("103", "Microservices Design", "System Arch", "10/03/2024", 1200, 400, 70, "", ""));
        similarDocuments.setValue(similar);
    }
}
