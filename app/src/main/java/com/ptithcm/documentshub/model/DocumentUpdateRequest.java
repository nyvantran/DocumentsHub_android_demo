package com.ptithcm.documentshub.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model request cho API cập nhật tài liệu.
 * Endpoint: PATCH /api/v1/documents/{document_id}
 *
 * Chỉ gửi các field cần thay đổi, các field không set sẽ là null
 * và Gson sẽ bỏ qua khi serialize (cần cấu hình serializeNulls = false).
 * Backend sử dụng exclude_unset=True nên chỉ cập nhật các field được gửi.
 */
public class DocumentUpdateRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("category_id")
    private Integer categoryId;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("tags")
    private List<String> tags;

    // ========== Setters (chỉ set các field cần update) ==========

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // ========== Getters ==========

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getVisibility() {
        return visibility;
    }

    public List<String> getTags() {
        return tags;
    }
}
