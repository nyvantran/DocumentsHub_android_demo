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
    private MutableLiveData<List<Document>> likedDocuments = new MutableLiveData<>();
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

    public LiveData<List<Document>> getLikedDocuments() {
        return likedDocuments;
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

    public void createCollection(String name) {
        isLoading.setValue(true);
        collectionRepository.createCollection(name, new Callback<ApiResponse<Collection>>() {
            @Override
            public void onResponse(Call<ApiResponse<Collection>> call, Response<ApiResponse<Collection>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    statusMessage.setValue("Đã thêm bộ sưu tập mới");
                    fetchMyCollections(); // Refresh the list
                } else {
                    statusMessage.setValue("Không thể tạo bộ sưu tập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Collection>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Lỗi: " + t.getMessage());
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

    public void deleteCollection(String collectionId) {
        isLoading.setValue(true);
        collectionRepository.deleteCollection(collectionId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    statusMessage.setValue("Đã xóa bộ sưu tập");
                    fetchMyCollections(); // Refresh the list
                } else {
                    statusMessage.setValue("Không thể xóa bộ sưu tập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Lỗi: " + t.getMessage());
            }
        });
    }

    public void removeItemFromCollection(String collectionId, String documentId) {
        isLoading.setValue(true);
        collectionRepository.removeItemFromCollection(collectionId, documentId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    statusMessage.setValue("Đã xóa tài liệu ra khỏi bộ sưu tập");
                    fetchCollectionItems(collectionId); // Refresh the list
                } else {
                    statusMessage.setValue("Không thể xóa tài liệu ra khỏi bộ sưu tập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Lỗi: " + t.getMessage());
            }
        });
    }

    public void fetchLikedDocuments() {
        isLoading.setValue(true);
        userRepository.getLikedDocuments(new Callback<ApiResponse<List<Document>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Document>>> call, Response<ApiResponse<List<Document>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    likedDocuments.setValue(response.body().getData());
                } else {
                    statusMessage.setValue("Failed to fetch liked documents");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Document>>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void unlikeDocument(String documentId) {
        isLoading.setValue(true);
        documentRepository.unlikeDocument(documentId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    statusMessage.setValue("Đã bỏ thích tài liệu");
                    fetchLikedDocuments(); // Refresh list
                } else {
                    statusMessage.setValue("Không thể bỏ thích tài liệu");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                statusMessage.setValue("Lỗi: " + t.getMessage());
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
