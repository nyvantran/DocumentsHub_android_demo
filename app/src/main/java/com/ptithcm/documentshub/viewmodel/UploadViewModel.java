package com.ptithcm.documentshub.viewmodel;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.CategoryRepository;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel xử lý logic upload tài liệu và tải danh sách categories từ API.
 * Quản lý trạng thái loading, kết quả upload và danh sách lỗi.
 */
public class UploadViewModel extends ViewModel {

    private static final String TAG = "UploadViewModel";

    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;

    // Trạng thái loading
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // Kết quả upload: true = thành công, false = thất bại
    private final MutableLiveData<Boolean> uploadResult = new MutableLiveData<>();

    // Thông báo lỗi
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Danh sách categories từ API
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public UploadViewModel() {
        this.documentRepository = new DocumentRepository();
        this.categoryRepository = new CategoryRepository();
    }

    // ========== Getters cho LiveData ==========

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getUploadResult() {
        return uploadResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    /**
     * Tải danh sách categories từ API để hiển thị trong Spinner.
     */
    public void fetchCategories() {
        categoryRepository.getCategories(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Category>>> call,
                                   @NonNull Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    categories.postValue(response.body().getData());
                } else {
                    Log.e(TAG, "Lỗi khi tải danh sách categories");
                    errorMessage.postValue("Không thể tải danh sách danh mục");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Category>>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Lỗi kết nối khi tải categories", t);
                errorMessage.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Upload tài liệu lên server.
     * Build MultipartBody thủ công để kiểm soát hoàn toàn cách gửi dữ liệu,
     * đảm bảo tương thích với FastAPI backend.
     *
     * @param fileUri         Uri của file đã chọn
     * @param title           Tiêu đề tài liệu
     * @param categoryId      ID danh mục
     * @param visibility      Quyền truy cập (PUBLIC hoặc PRIVATE)
     * @param desc            Mô tả (có thể null)
     * @param tags            Danh sách tags
     * @param contentResolver ContentResolver để đọc file từ Uri
     */
    public void uploadDocument(Uri fileUri, String title, int categoryId,
                               String visibility, String desc, List<String> tags,
                               ContentResolver contentResolver) {
        isLoading.setValue(true);

        // Lấy tên file gốc từ Uri
        String fileName = getFileNameFromUri(fileUri, contentResolver);
        if (fileName == null) {
            fileName = "document";
        }

        // Lấy MIME type của file
        String mimeType = contentResolver.getType(fileUri);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // Đọc file content từ Uri
        byte[] fileBytes;
        try {
            InputStream inputStream = contentResolver.openInputStream(fileUri);
            if (inputStream == null) {
                isLoading.setValue(false);
                errorMessage.setValue("Không thể đọc file đã chọn");
                return;
            }
            fileBytes = readInputStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Lỗi đọc file", e);
            isLoading.setValue(false);
            errorMessage.setValue("Lỗi đọc file: " + e.getMessage());
            return;
        }

        // Build MultipartBody thủ công để tương thích FastAPI
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // File part (bắt buộc)
        RequestBody fileBody = RequestBody.create(MediaType.parse(mimeType), fileBytes);
        builder.addFormDataPart("file", fileName, fileBody);

        // Text fields (bắt buộc)
        builder.addFormDataPart("title", title);
        builder.addFormDataPart("category_id", String.valueOf(categoryId));
        builder.addFormDataPart("visibility", visibility);

        // Desc (chỉ gửi khi không null và không rỗng)
        if (desc != null && !desc.isEmpty()) {
            builder.addFormDataPart("desc", desc);
        }

        // Tags (gửi từng tag riêng lẻ với cùng key "tags")
        if (tags != null) {
            for (String tag : tags) {
                if (tag != null && !tag.trim().isEmpty()) {
                    builder.addFormDataPart("tags", tag.trim().toLowerCase());
                }
            }
        }

        MultipartBody requestBody = builder.build();

        // Gọi API upload
        documentRepository.uploadDocument(requestBody, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                   @NonNull Response<ApiResponse<Void>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    uploadResult.postValue(true);
                } else {
                    uploadResult.postValue(false);
                    // Parse error từ errorBody khi backend trả 4xx
                    String msg = parseErrorMessage(response);
                    Log.e(TAG, "Upload failed: " + msg);
                    errorMessage.postValue(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call,
                                  @NonNull Throwable t) {
                isLoading.postValue(false);
                uploadResult.postValue(false);
                Log.e(TAG, "Lỗi kết nối khi upload", t);
                errorMessage.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Parse error message từ response.
     * Khi backend trả 4xx, Retrofit đặt lỗi vào errorBody() thay vì body().
     */
    private <T> String parseErrorMessage(Response<T> response) {
        // Thử lấy message từ body (2xx nhưng success=false)
        if (response.body() instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) response.body();
            if (apiResponse.getMessage() != null) {
                return apiResponse.getMessage();
            }
        }

        // Thử đọc errorBody (4xx/5xx)
        ResponseBody errorBody = response.errorBody();
        if (errorBody != null) {
            try {
                String errorJson = errorBody.string();
                Log.e(TAG, "Error body: " + errorJson);
                return "Upload thất bại (HTTP " + response.code() + "): " + errorJson;
            } catch (IOException e) {
                Log.e(TAG, "Lỗi đọc error body", e);
            }
        }

        return "Upload thất bại (HTTP " + response.code() + ")";
    }

    /**
     * Lấy tên file từ Uri thông qua ContentResolver.
     */
    private String getFileNameFromUri(Uri uri, ContentResolver contentResolver) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Lỗi lấy tên file", e);
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    /**
     * Đọc toàn bộ InputStream thành mảng byte.
     */
    private byte[] readInputStream(InputStream inputStream) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
