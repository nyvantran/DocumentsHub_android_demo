package com.ptithcm.documentshub.network;

import android.content.Context;

import com.ptithcm.documentshub.utils.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static ApiClient instance;
    private static Retrofit retrofit = null;

    private ApiClient(Context context) {
        TokenManager tokenManager = TokenManager.getInstance(context);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(tokenManager);
        AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(tokenAuthenticator)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


    }

    public static String getBaseUrl() {
        return BASE_URL.replace("http://", "").replace(":8000/", "");
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "RetrofitClient chưa được khởi tạo. Gọi RetrofitClient.init(context) trước.");
        }
        return instance;
    }

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
