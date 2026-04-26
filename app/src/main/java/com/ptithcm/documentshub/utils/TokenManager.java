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

    private static final long EXPIRY_DURATION = 30 * 60 * 1000;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static volatile TokenManager instance;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static TokenManager getInstance(Context context) {
        if (instance == null) {
            synchronized (TokenManager.class) {
                if (instance == null) {
                    instance = new TokenManager(context);
                }
            }
        }
        return instance;
    }

    public void saveTokens(String accessToken, String refreshToken) {
        long expiryTime = System.currentTimeMillis() + EXPIRY_DURATION;
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putLong(KEY_EXPIRY_TIME, expiryTime);
        editor.apply();
    }

    public void saveAccessToken(String accessToken) {
        long expiryTime = System.currentTimeMillis() + EXPIRY_DURATION;
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putLong(KEY_EXPIRY_TIME, expiryTime);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }


    public void clearTokens() {
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_EXPIRY_TIME);
        editor.apply();
    }
}
