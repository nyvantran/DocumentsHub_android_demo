package com.ptithcm.documentshub.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.CategorySectionAdapter;
import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị nội dung màn hình chính với ListView động cho các Category và Trending items.
 */
public class HomeFragment extends Fragment {

    private ListView lvMainContent;
    private CategorySectionAdapter mainAdapter;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        lvMainContent = view.findViewById(R.id.lv_main_content);
        
        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Thiết lập Adapter ban đầu với danh sách trống
        mainAdapter = new CategorySectionAdapter(requireContext(), new ArrayList<>());
        lvMainContent.setAdapter(mainAdapter);
        
        // Quan sát dữ liệu từ ViewModel
        observeViewModel();
        
        // Gọi dữ liệu
        viewModel.fetchHomeData();
        
        return view;
    }

    private void observeViewModel() {
        viewModel.getCategorySections().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                mainAdapter.updateData(categories);
            }
        });
    }
}