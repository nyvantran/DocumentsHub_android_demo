package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.model.User;
import com.ptithcm.documentshub.model.UserProfileUpdateRequest;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.CollectionRepository;
import com.ptithcm.documentshub.repository.DocumentRepository;
import com.ptithcm.documentshub.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private DocumentRepository documentRepository;
    private CollectionRepository collectionRepository;
    private UserRepository userRepository;
    private MutableLiveData<List<Document>> deletedDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Document>> myDocuments = new MutableLiveData<>();
    private MutableLiveData<List<Collection>> myCollections = new MutableLiveData<>();
    private MutableLiveData<List<Document>> collectionDocuments = new MutableLiveData<>();
    private MutableLiveData<User> userProfile = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<User>> updateProfileResult = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<String>> updateAvatarResult = new MutableLiveData<>();
    private MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ProfileViewModel() {
        documentRepository = new DocumentRepository();
        collectionRepository = new CollectionRepository();
        userRepository = new UserRepository();
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

    public LiveData<User> getUserProfile() {
        return userProfile;
    }

    public LiveData<ApiResponse<User>> getUpdateProfileResult() {
        return updateProfileResult;
    }

    public LiveData<ApiResponse<String>> getUpdateAvatarResult() {
        return updateAvatarResult;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchUserProfile() {
        isLoading.setValue(true);
        userRepository.getMyProfile(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    userProfile.setValue(response.body().getData());
                } else {
                    statusMessage.setValue("Failed to fetch profile");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void updateUserProfile(String fullName, String gender, String phoneNumber, String bio) {
        isLoading.setValue(true);
        UserProfileUpdateRequest request = new UserProfileUpdateRequest(fullName, gender, phoneNumber, bio);
        userRepository.updateMyProfile(request, new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    updateProfileResult.setValue(response.body());
                    if (response.body().isSuccess()) {
                        userProfile.setValue(response.body().getData());
                    }
                } else {
                    statusMessage.setValue("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void updateAvatar(MultipartBody.Part avatar) {
        isLoading.setValue(true);
        userRepository.updateAvatar(avatar, new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateAvatarResult.setValue(response.body());
                    if (response.body().isSuccess()) {
                        // Thay vì phân giải JSON String, ta gọi lại API profile để đồng bộ chuẩn xác nhất
                        fetchUserProfile();
                    } else {
                        isLoading.setValue(false);
                        statusMessage.setValue("Failed to update avatar");
                    }
                } else {
                    isLoading.setValue(false);
                    statusMessage.setValue("Failed to update avatar");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void clearUpdateResults() {
        updateProfileResult.setValue(null);
        updateAvatarResult.setValue(null);

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
}
