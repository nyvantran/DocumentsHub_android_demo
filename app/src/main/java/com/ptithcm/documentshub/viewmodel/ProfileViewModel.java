package com.ptithcm.documentshub.viewmodel;

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

public class ProfileViewModel extends ViewModel {
    private DocumentRepository documentRepository;
    private CollectionRepository collectionRepository;
    private MutableLiveData<List<Document>> deletedDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Document>> myDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Collection>> myCollections = new MutableLiveData<>();
    private MutableLiveData<List<Document>> collectionDocuments = new MutableLiveData<>();
    private MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ProfileViewModel() {
        documentRepository = new DocumentRepository();
        collectionRepository = new CollectionRepository();
    }

    public LiveData<List<Document>> getDeletedDocuments() {
        return deletedDocuments;
    }

    public LiveData<List<Document>> getMyDocuments() {
        return myDocuments;
    }

    public LiveData<List<Collection>> getMyCollections() {
        return myCollections;
    }

    public LiveData<List<Document>> getCollectionDocuments() {
        return collectionDocuments;
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

    public void fetchMyCollections() {
        isLoading.setValue(true);
        collectionRepository.getMyCollections(1, 10, new Callback<ApiResponse<List<Collection>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Collection>>> call, Response<ApiResponse<List<Collection>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    myCollections.setValue(response.body().getData());
                } else {
                    statusMessage.setValue("Failed to fetch collections");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Collection>>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error fetching collections: " + t.getMessage());
            }
        });
    }

    public void fetchCollectionItems(String collectionId) {
        isLoading.setValue(true);
        collectionDocuments.setValue(new ArrayList<>()); // Clear old data
        collectionRepository.getCollectionItems(collectionId, new Callback<ApiResponse<List<Document>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Document>>> call, Response<ApiResponse<List<Document>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    collectionDocuments.setValue(response.body().getData());
                } else {
                    statusMessage.setValue("Failed to fetch collection documents");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Document>>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    private void fetchDocumentsByStatus(String status, MutableLiveData<List<Document>> liveData) {
        isLoading.setValue(true);
        documentRepository.getMyDocuments(10, status, new Callback<ApiResponse<List<Document>>>() {
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
        documentRepository.restoreDocument(document.getId(), new Callback<ApiResponse<Void>>() {
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

    /**
     * Xóa mềm tài liệu (soft delete — chuyển vào thùng rác).
     * Sử dụng optimistic update: xóa khỏi list ngay lập tức để UI cập nhật tức thì,
     * sau đó gọi API. Nếu API thất bại, thêm lại item vào list.
     *
     * @param document Tài liệu cần xóa
     */
    public void deleteDocument(Document document) {
        // Optimistic update: xóa khỏi list local ngay lập tức
        List<Document> currentList = myDocuments.getValue();
        if (currentList != null) {
            List<Document> updatedList = new ArrayList<>(currentList);
            updatedList.remove(document);
            myDocuments.setValue(updatedList);
        }

        // Gọi API xóa ở background
        documentRepository.deleteDocument(document.getId(), new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    statusMessage.setValue("Đã xóa tài liệu thành công");
                } else {
                    // API thất bại: thêm lại item vào list
                    restoreItemToList(document);
                    statusMessage.setValue("Không thể xóa tài liệu");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                // Lỗi kết nối: thêm lại item vào list
                restoreItemToList(document);
                statusMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Thêm lại document vào danh sách khi API xóa thất bại.
     */
    private void restoreItemToList(Document document) {
        List<Document> currentList = myDocuments.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        List<Document> restoredList = new ArrayList<>(currentList);
        restoredList.add(document);
        myDocuments.setValue(restoredList);
    }
}
