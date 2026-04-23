package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentViewModel extends ViewModel {
    private DocumentRepository repository;
    private MutableLiveData<Document> document = new MutableLiveData<>();
    private MutableLiveData<List<Document>> similarDocuments = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public DocumentViewModel() {
        repository = new DocumentRepository();
    }

    public LiveData<Document> getDocument() {
        return document;
    }

    public LiveData<List<Document>> getSimilarDocuments() {
        return similarDocuments;
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
