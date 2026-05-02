package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ProfileViewModel extends ViewModel {
    private DocumentRepository repository;
    private MutableLiveData<List<Document>> deletedDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Document>> myDocuments = new MutableLiveData<>();
    private MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ProfileViewModel() {
        repository = new DocumentRepository();
    }

    public LiveData<List<Document>> getDeletedDocuments() {
        return deletedDocuments;
    }

    public LiveData<List<Document>> getMyDocuments() {
        return myDocuments;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchDeletedDocuments() {
        fetchDocumentsByStatus("DELETED", deletedDocuments);
    }

    public void fetchReadyDocuments() {
        fetchDocumentsByStatus("READY", myDocuments);
    }

    private void fetchDocumentsByStatus(String status, MutableLiveData<List<Document>> liveData) {
        isLoading.setValue(true);
        repository.getMyDocuments(10, status, new Callback<ApiResponse<List<Document>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Document>>> call, Response<ApiResponse<List<Document>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.setValue(response.body().getData());
                } else {
                    statusMessage.setValue("Failed to fetch documents: " + status);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Document>>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void restoreDocument(Document document) {

        isLoading.setValue(true);
        repository.restoreDocument(document.getId(), new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    statusMessage.setValue("Document restored successfully");
                    fetchDeletedDocuments(); // Refresh list
                } else {
                    statusMessage.setValue("Failed to restore document");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }
}
