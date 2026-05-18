package com.ptithcm.documentshub.fragment;

import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.CategoryAdapter;
import com.ptithcm.documentshub.adapter.CategorySectionAdapter;
import com.ptithcm.documentshub.viewmodel.HomeViewModel;

import java.util.ArrayList;

/**
 * Fragment hiển thị nội dung màn hình chính với ListView động cho các Category và Trending items.
 */
public class HomeFragment extends Fragment {

    private ListView lvMainContent;
    private RecyclerView rvCategories;
    private EditText etSearch;
    private CategorySectionAdapter mainAdapter;
    private CategoryAdapter categoryAdapter;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        lvMainContent = view.findViewById(R.id.lv_main_content);
        rvCategories = view.findViewById(R.id.rv_categories);
        etSearch = view.findViewById(R.id.et_search);
        
        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Thiết lập Adapter ban đầu với danh sách trống
        mainAdapter = new CategorySectionAdapter(requireContext(), new ArrayList<>());
        lvMainContent.setAdapter(mainAdapter);

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), (category, position) -> {
            // Xử lý khi chọn category (ví dụ: lọc danh sách bên dưới)
            viewModel.filterByCategory(category.getId()); // Tìm kiếm theo category
        });
        rvCategories.setAdapter(categoryAdapter);
        
        // Fix bug: Prevent ViewPager2 from intercepting touch events when scrolling categories
        rvCategories.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull android.view.MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull android.view.MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
        
        setupSearch();
        
        // Quan sát dữ liệu từ ViewModel
        observeViewModel();
        
        // Gọi dữ liệu
        viewModel.fetchHomeData();
        
        return view;
    }

    private void setupSearch() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchDocuments(etSearch.getText().toString());
                hideKeyboard();
                return true;
            }
            return false;
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    viewModel.fetchHomeData();
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void observeViewModel() {
        viewModel.getCategorySections().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                mainAdapter.updateData(categories);
            }
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.updateData(categories);
            }
        });
    }
}