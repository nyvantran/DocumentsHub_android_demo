package com.ptithcm.documentshub.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.CategorySectionAdapter;
import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.model.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị nội dung màn hình chính với ListView động cho các Category và Trending items.
 */
public class HomeFragment extends Fragment {

    private ListView lvMainContent;
    private List<Category> categoryList;
    private CategorySectionAdapter mainAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        lvMainContent = view.findViewById(R.id.lv_main_content);
        
        // Khởi tạo dữ liệu dummy
        createDummyData();
        
        // Thiết lập Adapter
        mainAdapter = new CategorySectionAdapter(requireContext(), categoryList);
        lvMainContent.setAdapter(mainAdapter);
        
        return view;
    }

    private void createDummyData() {
        categoryList = new ArrayList<>();

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
    }
}