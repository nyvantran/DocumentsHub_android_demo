package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableCornerRadiusLiveData; // Wait, that's not right. Just MutableLiveData.
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<ApiResponse<Void>> forgotPasswordResponse = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<Void>> resetPasswordResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ForgotPasswordViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<ApiResponse<Void>> getForgotPasswordResponse() {
        return forgotPasswordResponse;
    }

    public LiveData<ApiResponse<Void>> getResetPasswordResponse() {
        return resetPasswordResponse;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void forgotPassword(String identity) {
        isLoading.setValue(true);
        authRepository.forgotPassword(identity, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    forgotPasswordResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Lỗi: Không tìm thấy người dùng hoặc lỗi hệ thống.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void resetPassword(String identity, String otpCode, String newPassword) {
        isLoading.setValue(true);
        authRepository.resetPassword(identity, otpCode, newPassword, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    resetPasswordResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Lỗi: OTP không đúng hoặc đã hết hạn.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
