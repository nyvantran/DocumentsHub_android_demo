package com.ptithcm.documentshub.network;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private T data;

    @SerializedName("message")
    private String message;

    @SerializedName("meta")
    private Meta meta;

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Meta getMeta() {
        return meta;
    }

    public static class Meta {
        @SerializedName("current_page")
        private int currentPage;

        @SerializedName("per_page")
        private int perPage;

        @SerializedName("total_items")
        private int totalItems;

        @SerializedName("total_pages")
        private int totalPages;

        @SerializedName("has_next")
        private boolean hasNext;

        @SerializedName("has_prev")
        private boolean hasPrev;

        // Getters
        public int getCurrentPage() { return currentPage; }
        public int getPerPage() { return perPage; }
        public int getTotalItems() { return totalItems; }
        public int getTotalPages() { return totalPages; }
        public boolean isHasNext() { return hasNext; }
        public boolean isHasPrev() { return hasPrev; }
    }
}
