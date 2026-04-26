package com.ptithcm.documentshub;

import android.app.Application;

import com.ptithcm.documentshub.network.ApiClient;

public class DocumentsHub extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo RetrofitClient một lần duy nhất với Application context
        ApiClient.init(this);
    }
}