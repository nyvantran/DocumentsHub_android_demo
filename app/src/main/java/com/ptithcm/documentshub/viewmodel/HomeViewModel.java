package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.CategoryRepository;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private DocumentRepository documentRepository;
    private CategoryRepository categoryRepository;
    private MutableLiveData<List<Category>> categorySections = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    private String currentQuery = "";
    private Integer currentCategoryId = null;

    public HomeViewModel() {
        documentRepository = new DocumentRepository();
        categoryRepository = new CategoryRepository();
    }

    public LiveData<List<Category>> getCategorySections() {
        return categorySections;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchDocuments(String query) {
        this.currentQuery = (query == null) ? "" : query.trim();
        executeSearch();
    }

    public void filterByCategory(Integer categoryId) {
        if (categoryId != null && categoryId == -1) {
            this.currentCategoryId = null;
        } else {
            this.currentCategoryId = categoryId;
        }
        executeSearch();
    }

    private void executeSearch() {
        String displayTitle = currentQuery.isEmpty() ? "Trending Documents" : "Search Results for \"" + currentQuery + "\"";

        documentRepository.searchDocumentsByQuery(currentQuery, currentCategoryId, 1, 20, "-view", new Callback<ApiResponse<List<Document>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Document>>> call, Response<ApiResponse<List<Document>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Document> docs = response.body().getData();
                    List<Category> searchResult = new ArrayList<>();
                    searchResult.add(new Category(displayTitle, docs));
                    categorySections.setValue(searchResult);
                } else {
                    errorMessage.setValue("Failed to fetch documents");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Document>>> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }

    public void fetchHomeData() {
        fetchCategories();
        searchDocuments(""); // Fetch trending documents by default
    }

    private void fetchCategories() {
        categoryRepository.getCategories(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = new ArrayList<>();
                    // Thêm phần tử mặc định ALL
                    Category allCategory = new Category("ALL", null);
                    allCategory.setId(-1); // ID đặc biệt cho ALL
                    categoryList.add(allCategory);
                    
                    if (response.body().getData() != null) {
                        categoryList.addAll(response.body().getData());
                    }
                    categories.setValue(categoryList);
                } else {
                    errorMessage.setValue("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
}
