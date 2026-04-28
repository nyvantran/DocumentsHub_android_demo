package com.ptithcm.documentshub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.activity.LoginActivity;

import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ptithcm.documentshub.adapter.ProfileCollectionAdapter;
import com.ptithcm.documentshub.adapter.ProfileDocumentAdapter;
import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private Button btnEditProfile;

    private LinearLayout tabOverview, tabDocuments, tabCollections, tabLiked, tabTrash;
    private TextView tvTabOverview, tvTabDocuments, tvTabCollections, tvTabLiked, tvTabTrash;
    private View indicatorOverview, indicatorDocuments, indicatorCollections, indicatorLiked, indicatorTrash;
    private FrameLayout tabContentContainer;

    private List<Document> myDocuments = new ArrayList<>();
    private List<Collection> myCollections = new ArrayList<>();
    private List<Document> likedDocuments = new ArrayList<>();
    private List<Document> trashDocuments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        btnLogout = view.findViewById(R.id.btn_logout);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        // Tabs
        tabOverview = view.findViewById(R.id.tab_overview);
        tabDocuments = view.findViewById(R.id.tab_documents);
        tabCollections = view.findViewById(R.id.tab_collections);
        tabLiked = view.findViewById(R.id.tab_liked);
        tabTrash = view.findViewById(R.id.tab_trash);

        tvTabOverview = view.findViewById(R.id.tv_tab_overview);
        tvTabDocuments = view.findViewById(R.id.tv_tab_documents);
        tvTabCollections = view.findViewById(R.id.tv_tab_collections);
        tvTabLiked = view.findViewById(R.id.tv_tab_liked);
        tvTabTrash = view.findViewById(R.id.tv_tab_trash);

        indicatorOverview = view.findViewById(R.id.indicator_overview);
        indicatorDocuments = view.findViewById(R.id.indicator_documents);
        indicatorCollections = view.findViewById(R.id.indicator_collections);
        indicatorLiked = view.findViewById(R.id.indicator_liked);
        indicatorTrash = view.findViewById(R.id.indicator_trash);

        tabContentContainer = view.findViewById(R.id.tab_content_container);

        prepareDummyData();
        setupListeners();
        
        // Default tab
        switchTab("overview");

        return view;
    }

    private void prepareDummyData() {
        // Dummy Documents
        myDocuments.add(new Document("1", "Giải tích 1", "Lê Ngọc Tú", "2024-04-20", 120, 15, 5, "", "Tài liệu giải tích 1 PTIT"));
        myDocuments.add(new Document("2", "Cấu trúc dữ liệu", "Lê Ngọc Tú", "2024-04-22", 80, 10, 2, "", "Slide bài giảng CTDL"));

        // Dummy Collections
        myCollections.add(new Collection("1", "Học tập", 5, ""));
        myCollections.add(new Collection("2", "Tham khảo", 3, ""));
        myCollections.add(new Collection("3", "Dự án", 2, ""));

        // Dummy Liked
        likedDocuments.add(new Document("3", "Kiến trúc phần mềm", "Nguyễn Văn A", "2024-04-15", 300, 50, 20, "", "Kiến trúc phần mềm"));
        
        // Dummy Trash
        trashDocuments.add(new Document("4", "Tài liệu cũ", "Lê Ngọc Tú", "2023-12-01", 10, 1, 0, "", "Cần xóa"));
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> logout());
        btnEditProfile.setOnClickListener(v -> {
            EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance();
            dialog.show(getChildFragmentManager(), "EditProfileDialog");
        });

        tabOverview.setOnClickListener(v -> switchTab("overview"));
        tabDocuments.setOnClickListener(v -> switchTab("documents"));
        tabCollections.setOnClickListener(v -> switchTab("collections"));
        tabLiked.setOnClickListener(v -> switchTab("liked"));
        tabTrash.setOnClickListener(v -> switchTab("trash"));
    }

    private void switchTab(String tab) {
        // Reset all tabs
        resetTabUI();

        tabContentContainer.removeAllViews();

        switch (tab) {
            case "overview":
                updateTabUI(tvTabOverview, indicatorOverview);
                showOverview();
                break;
            case "documents":
                updateTabUI(tvTabDocuments, indicatorDocuments);
                showDocuments();
                break;
            case "collections":
                updateTabUI(tvTabCollections, indicatorCollections);
                showCollections();
                break;
            case "liked":
                updateTabUI(tvTabLiked, indicatorLiked);
                showLiked();
                break;
            case "trash":
                updateTabUI(tvTabTrash, indicatorTrash);
                showTrash();
                break;
        }
    }

    private void resetTabUI() {
        int secondaryColor = ContextCompat.getColor(requireContext(), R.color.text_secondary);
        tvTabOverview.setTextColor(secondaryColor);
        tvTabDocuments.setTextColor(secondaryColor);
        tvTabCollections.setTextColor(secondaryColor);
        tvTabLiked.setTextColor(secondaryColor);
        tvTabTrash.setTextColor(secondaryColor);

        indicatorOverview.setVisibility(View.INVISIBLE);
        indicatorDocuments.setVisibility(View.INVISIBLE);
        indicatorCollections.setVisibility(View.INVISIBLE);
        indicatorLiked.setVisibility(View.INVISIBLE);
        indicatorTrash.setVisibility(View.INVISIBLE);
    }

    private void updateTabUI(TextView textView, View indicator) {
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_text_selected));
        indicator.setVisibility(View.VISIBLE);
    }

    private void showOverview() {
        TextView tvBio = new TextView(getContext());
        tvBio.setText(getString(R.string.dummy_user_bio));
        tvBio.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        tvBio.setTextSize(14);
        tabContentContainer.addView(tvBio);
    }

    private void showDocuments() {
        ListView listView = new ListView(getContext());
        listView.setDivider(null);
        ProfileDocumentAdapter adapter = new ProfileDocumentAdapter(getContext(), myDocuments);
        listView.setAdapter(adapter);
        tabContentContainer.addView(listView);
        setListViewHeightBasedOnChildren(listView);
    }

    private void showCollections() {
        GridView gridView = new GridView(getContext());
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing(8);
        gridView.setHorizontalSpacing(8);
        ProfileCollectionAdapter adapter = new ProfileCollectionAdapter(getContext(), myCollections);
        gridView.setAdapter(adapter);
        
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Collection selectedCollection = myCollections.get(position);
            showCollectionDetail(selectedCollection);
        });

        tabContentContainer.addView(gridView);
        setGridViewHeightBasedOnChildren(gridView, 2);
    }

    private void showCollectionDetail(Collection collection) {
        tabContentContainer.removeAllViews();
        View detailView = LayoutInflater.from(getContext()).inflate(R.layout.layout_collection_detail, tabContentContainer, false);
        
        ImageView btnBack = detailView.findViewById(R.id.btn_back_to_collections);
        TextView tvTitle = detailView.findViewById(R.id.tv_collection_detail_title);
        ListView listView = detailView.findViewById(R.id.lv_collection_documents);

        tvTitle.setText(collection.getName());
        
        // Sử dụng dữ liệu mẫu cho tài liệu trong bộ sưu tập
        ProfileDocumentAdapter adapter = new ProfileDocumentAdapter(getContext(), myDocuments);
        listView.setAdapter(adapter);
        
        btnBack.setOnClickListener(v -> switchTab("collections"));

        tabContentContainer.addView(detailView);
        setListViewHeightBasedOnChildren(listView);
    }

    private void showLiked() {
        ListView listView = new ListView(getContext());
        listView.setDivider(null);
        ProfileDocumentAdapter adapter = new ProfileDocumentAdapter(getContext(), likedDocuments);
        listView.setAdapter(adapter);
        tabContentContainer.addView(listView);
        setListViewHeightBasedOnChildren(listView);
    }

    private void showTrash() {
        ListView listView = new ListView(getContext());
        listView.setDivider(null);
        ProfileDocumentAdapter adapter = new ProfileDocumentAdapter(getContext(), trashDocuments);
        listView.setAdapter(adapter);
        tabContentContainer.addView(listView);
        setListViewHeightBasedOnChildren(listView);
    }

    // Helper to set ListView height based on children to work inside ScrollView
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = (int) Math.ceil((double) items / columns);

        for (int i = 0; i < rows; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (8 * (rows - 1)); // 8 is vertical spacing
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    private void logout() {
        // Trong thực tế, bạn sẽ xóa session/token ở đây
        Toast.makeText(getContext(), "Đang đăng xuất...", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Xóa sạch stack của activity cũ để không back lại được HomeActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}