package com.ptithcm.documentshub.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.network.ApiResponse;
import com.ptithcm.documentshub.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private DocumentRepository repository;
    private MutableLiveData<List<Category>> categorySections = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel() {
        repository = new DocumentRepository();
    }

    public LiveData<List<Category>> getCategorySections() {
        return categorySections;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchHomeData() {
        // Tạm thời gọi dummy data vì API thật chưa được thiết lập URL chính xác
        loadDummyData();
    }

    private void loadDummyData() {
        List<Category> categoryList = new ArrayList<>();

        // Danh mục Computer
        List<Document> computerDocs = new ArrayList<>();
        computerDocs.add(new Document("Kiến trúc phần mềm", "tule193", "Public", 11, "Computer"));
        computerDocs.add(new Document("Hệ điều hành", "admin", "Public", 45, "Computer"));
        categoryList.add(new Category("Computer", computerDocs));

        // Danh mục Programming
        List<Document> programmingDocs = new ArrayList<>();
        programmingDocs.add(new Document("Java Core for Beginners", "java_master", "Public", 120, "Programming"));
        programmingDocs.add(new Document("Android Development Guide", "ptit_student", "Public", 85, "Programming"));
        programmingDocs.add(new Document("C++ Data Structures", "prof_x", "Public", 200, "Programming"));
        categoryList.add(new Category("Programming", programmingDocs));

        // Danh mục Science
        List<Document> scienceDocs = new ArrayList<>();
        scienceDocs.add(new Document("Quantum Physics", "einstein", "Public", 350, "Science"));
        scienceDocs.add(new Document("Introduction to Biology", "darwin", "Public", 150, "Science"));
        categoryList.add(new Category("Science", scienceDocs));

        categorySections.setValue(categoryList);
    }
}
