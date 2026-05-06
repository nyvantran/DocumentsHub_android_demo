package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.model.RegisterVerifyResponse;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<ApiResponse<Void>> registerRequestResponse = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<RegisterVerifyResponse>> registerVerifyResponse = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<LoginResponse>> registerCompleteResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignUpViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<ApiResponse<Void>> getRegisterRequestResponse() {
        return registerRequestResponse;
    }

    public LiveData<ApiResponse<RegisterVerifyResponse>> getRegisterVerifyResponse() {
        return registerVerifyResponse;
    }

    public LiveData<ApiResponse<LoginResponse>> getRegisterCompleteResponse() {
        return registerCompleteResponse;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void registerRequest(String email) {
        isLoading.setValue(true);
        authRepository.registerRequest(email, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    registerRequestResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Lỗi: Email đã tồn tại hoặc không hợp lệ.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void registerVerify(String email, String otpCode) {
        isLoading.setValue(true);
        authRepository.registerVerify(email, otpCode, new Callback<ApiResponse<RegisterVerifyResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RegisterVerifyResponse>> call, Response<ApiResponse<RegisterVerifyResponse>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    registerVerifyResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Lỗi: Mã OTP không chính xác hoặc đã hết hạn.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RegisterVerifyResponse>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void registerComplete(String email, String registrationCode, String username, String password) {
        isLoading.setValue(true);
        authRepository.registerComplete(email, registrationCode, username, password, new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    registerCompleteResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Lỗi: Đăng ký không thành công. Vui lòng kiểm tra lại thông tin.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
