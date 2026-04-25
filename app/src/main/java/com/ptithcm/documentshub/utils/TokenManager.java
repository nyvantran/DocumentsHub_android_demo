package com.ptithcm.documentshub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ptithcm.documentshub.model.LoginResponse;
import com.ptithcm.documentshub.model.RefreshRequest;
import com.ptithcm.documentshub.network.ApiClient;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.network.api.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenManager {
    private static final String PREF_NAME = "DocumentHubPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_EXPIRY_TIME = "expiry_time";

    // Giả sử token hết hạn sau 30 phút (1800000 ms)
    private static final long EXPIRY_DURATION = 30 * 60 * 1000;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveTokens(String accessToken, String refreshToken) {
        long expiryTime = System.currentTimeMillis() + EXPIRY_DURATION;
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putLong(KEY_EXPIRY_TIME, expiryTime);
        editor.apply();
    }

    public String getAccessToken() {
        if (shouldRefresh()) {
            refreshAccessTokenSync();
        }
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    private boolean shouldRefresh() {
        long expiryTime = sharedPreferences.getLong(KEY_EXPIRY_TIME, 0);
        // Refresh nếu còn dưới 5 phút là hết hạn
        return System.currentTimeMillis() > (expiryTime - 5 * 60 * 1000);
    }

    public void refreshAccessTokenSync() {
        String refreshToken = getRefreshToken();
        if (refreshToken == null) return;

        AuthService authService = ApiClient.createService(AuthService.class);
        Call<ApiResponse<LoginResponse>> call = authService.refresh(new RefreshRequest(refreshToken));

        try {
            // Thực hiện gọi đồng bộ vì chúng ta cần token ngay lập tức cho request hiện tại
            Response<ApiResponse<LoginResponse>> response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                LoginResponse data = response.body().getData();
                saveTokens(data.getAccessToken(), data.getRefreshToken());
                Log.d("TokenManager", "Token refreshed successfully");
            } else {
                Log.e("TokenManager", "Failed to refresh token");
            }
        } catch (Exception e) {
            Log.e("TokenManager", "Error refreshing token", e);
        }
    }

    public void clearTokens() {
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_EXPIRY_TIME);
        editor.apply();
    }
}
