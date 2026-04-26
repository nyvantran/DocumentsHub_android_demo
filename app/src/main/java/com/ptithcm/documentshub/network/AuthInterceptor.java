package com.ptithcm.documentshub.network;

import com.ptithcm.documentshub.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    private TokenManager tokenManager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.url().encodedPath().contains("api/v1/auth/login") ||
                originalRequest.url().encodedPath().contains("api/v1/auth/refresh")) {
            return chain.proceed(originalRequest);
        }
        String accessToken = tokenManager.getAccessToken();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        if (accessToken != null) {
            requestBuilder.header(HEADER_AUTHORIZATION, "Bearer " + accessToken);
        }
        return chain.proceed(requestBuilder.build());
    }
}
