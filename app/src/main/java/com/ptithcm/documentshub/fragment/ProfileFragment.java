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
import com.ptithcm.documentshub.activity.DocumentActivity;

import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ptithcm.documentshub.adapter.ProfileCollectionAdapter;
import com.ptithcm.documentshub.adapter.ProfileDocumentAdapter;
import com.ptithcm.documentshub.adapter.TrashDocumentAdapter;
import com.ptithcm.documentshub.model.Collection;
import com.ptithcm.documentshub.model.Document;
import com.ptithcm.documentshub.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AlertDialog;

public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private Button btnEditProfile;

    private LinearLayout tabOverview, tabDocuments, tabCollections, tabLiked, tabTrash;
    private TextView tvTabOverview, tvTabDocuments, tvTabCollections, tvTabLiked, tvTabTrash;
    private View indicatorOverview, indicatorDocuments, indicatorCollections, indicatorLiked, indicatorTrash;
    private FrameLayout tabContentContainer;

    private List<Document> myDocumentsDummy = new ArrayList<>();
    private List<Document> likedDocuments = new ArrayList<>();

    private ProfileViewModel profileViewModel;
    private TrashDocumentAdapter trashAdapter;
    private ProfileDocumentAdapter profileDocumentAdapter;
    private ProfileCollectionAdapter profileCollectionAdapter;
    private ProfileDocumentAdapter collectionItemsAdapter;
    private ListView lvCollectionItems; // Để cập nhật chiều cao sau khi load data
    private ListView lvDocuments; // Để cập nhật chiều cao sau khi xóa document

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

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
        setupViewModelObservers();

        // Default tab
        switchTab("overview");

        return view;
    }

    private void setupViewModelObservers() {
        profileViewModel.getStatusMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getDeletedDocuments().observe(getViewLifecycleOwner(), documents -> {
            if (documents != null && trashAdapter != null) {
                trashAdapter.updateData(documents);
            }
        });

        profileViewModel.getMyDocuments().observe(getViewLifecycleOwner(), documents -> {
            if (documents != null && profileDocumentAdapter != null) {
                profileDocumentAdapter.updateData(documents);
                // Tính lại chiều cao ListView sau khi data thay đổi (xóa, refresh)
                if (lvDocuments != null) {
                    setListViewHeightBasedOnChildren(lvDocuments);
                }
            }
        });

        profileViewModel.getMyCollections().observe(getViewLifecycleOwner(), collections -> {
            if (collections != null && profileCollectionAdapter != null) {
                profileCollectionAdapter.updateData(collections);
            }
        });

        profileViewModel.getCollectionDocuments().observe(getViewLifecycleOwner(), documents -> {
            if (documents != null && collectionItemsAdapter != null) {
                collectionItemsAdapter.updateData(documents);
                if (lvCollectionItems != null) {
                    setListViewHeightBasedOnChildren(lvCollectionItems);
                }
            }
        });
    }

    private void prepareDummyData() {
        // Dummy Documents for other tabs
        myDocumentsDummy.add(new Document("1", "Giải tích 1", "Lê Ngọc Tú", "2024-04-20", 120, 15, 5, "", "Tài liệu giải tích 1 PTIT"));
        myDocumentsDummy.add(new Document("2", "Cấu trúc dữ liệu", "Lê Ngọc Tú", "2024-04-22", 80, 10, 2, "", "Slide bài giảng CTDL"));

        // Dummy Liked
        likedDocuments.add(new Document("3", "Kiến trúc phần mềm", "Nguyễn Văn A", "2024-04-15", 300, 50, 20, "", "Kiến trúc phần mềm"));
        profileViewModel.fetchDeletedDocuments();
        profileViewModel.fetchReadyDocuments();
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
        lvDocuments = new ListView(getContext());
        lvDocuments.setDivider(null);

        List<Document> initialData = profileViewModel.getMyDocuments().getValue();
        if (initialData == null) initialData = new ArrayList<>();

        profileDocumentAdapter = new ProfileDocumentAdapter(getContext(), initialData,
                document -> {
                    // Confirm delete
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa tài liệu \"" + document.getTitle() + "\" không?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                profileViewModel.deleteDocument(document);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                },
                document -> {
                    // Navigate to DocumentActivity
                    Intent intent = new Intent(getActivity(), DocumentActivity.class);
                    intent.putExtra("DOCUMENT_ID", document.getId());
                    startActivity(intent);
                }
        );

        lvDocuments.setAdapter(profileDocumentAdapter);
        tabContentContainer.addView(lvDocuments);

        // Fetch fresh READY documents
        profileViewModel.fetchReadyDocuments();

        setListViewHeightBasedOnChildren(lvDocuments);
    }

    private void showCollections() {
        // Main container for the collections tab
        LinearLayout mainContainer = new LinearLayout(getContext());
        mainContainer.setOrientation(LinearLayout.VERTICAL);

        // Header with "Your Collections" and "+" button
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_profile_collections_header, mainContainer, false);
        ImageButton btnAddCollection = headerView.findViewById(R.id.btn_add_collection);
        btnAddCollection.setOnClickListener(v -> showAddCollectionDialog());

        // GridView for collections
        GridView gridView = new GridView(getContext());
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing(16);
        gridView.setHorizontalSpacing(16);

        List<Collection> initialData = profileViewModel.getMyCollections().getValue();
        if (initialData == null) initialData = new ArrayList<>();
        profileCollectionAdapter = new ProfileCollectionAdapter(getContext(), initialData,
                collection -> {
                    // Confirm delete
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa collection này không?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                Toast.makeText(getContext(), "đã xóa collection có id=" + collection.getId(), Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                },
                collection -> {
                    showCollectionDetail(collection);
                }
        );
        gridView.setAdapter(profileCollectionAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Collection selectedCollection = (Collection) profileCollectionAdapter.getItem(position);
            showCollectionDetail(selectedCollection);
        });

        mainContainer.addView(headerView);
        mainContainer.addView(gridView);

        tabContentContainer.addView(mainContainer);
        setGridViewHeightBasedOnChildren(gridView, 2);

        profileViewModel.fetchMyCollections();
    }

    private void showAddCollectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_collection, null);
        builder.setView(dialogView);

        final EditText etCollectionName = dialogView.findViewById(R.id.et_collection_name);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogView.findViewById(R.id.btn_add_collection);

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            String collectionName = etCollectionName.getText().toString().trim();
            if (!collectionName.isEmpty()) {
                Toast.makeText(getContext(), "đã thêm collection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // Here you would typically call a viewModel method to add the collection
                // profileViewModel.addCollection(collectionName);
            } else {
                Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showCollectionDetail(Collection collection) {
        tabContentContainer.removeAllViews();
        View detailView = LayoutInflater.from(getContext()).inflate(R.layout.layout_collection_detail, tabContentContainer, false);

        ImageView btnBack = detailView.findViewById(R.id.btn_back_to_collections);
        TextView tvTitle = detailView.findViewById(R.id.tv_collection_detail_title);
        lvCollectionItems = detailView.findViewById(R.id.lv_collection_documents);

        tvTitle.setText(collection.getName());

        // Khởi tạo adapter với danh sách rỗng ban đầu hoặc dữ liệu đã có trong ViewModel
        List<Document> currentDocs = profileViewModel.getCollectionDocuments().getValue();
        if (currentDocs == null) currentDocs = new ArrayList<>();

        collectionItemsAdapter = new ProfileDocumentAdapter(getContext(), currentDocs,
                document -> {
                    Toast.makeText(getContext(), "đã xóa tài liệu có id=" + document.getId() + " ra khỏi collection ", Toast.LENGTH_SHORT).show();
                },
                document -> {
                    Intent intent = new Intent(getActivity(), DocumentActivity.class);
                    intent.putExtra("DOCUMENT_ID", document.getId());
                    startActivity(intent);
                }
        );
        lvCollectionItems.setAdapter(collectionItemsAdapter);

        btnBack.setOnClickListener(v -> {
            lvCollectionItems = null; // Dọn dẹp reference
            collectionItemsAdapter = null;
            switchTab("collections");
        });

        tabContentContainer.addView(detailView);

        // Gọi API lấy dữ liệu thực tế
        profileViewModel.fetchCollectionItems(collection.getId());

        setListViewHeightBasedOnChildren(lvCollectionItems);
    }

    private void showLiked() {
        ListView listView = new ListView(getContext());
        listView.setDivider(null);
        ProfileDocumentAdapter adapter = new ProfileDocumentAdapter(getContext(), likedDocuments,
                document -> {
                    Toast.makeText(getContext(), "đã xóa tài liệu có id=" + document.getId(), Toast.LENGTH_SHORT).show();
                },
                document -> {
                    Intent intent = new Intent(getActivity(), DocumentActivity.class);
                    intent.putExtra("DOCUMENT_ID", document.getId());
                    startActivity(intent);
                }
        );
        listView.setAdapter(adapter);
        tabContentContainer.addView(listView);
        setListViewHeightBasedOnChildren(listView);
    }

    private void showTrash() {
        ListView listView = new ListView(getContext());
        listView.setDivider(null);

        List<Document> initialData = profileViewModel.getDeletedDocuments().getValue();
        if (initialData == null) initialData = new ArrayList<>();

        trashAdapter = new TrashDocumentAdapter(getContext(), initialData,
                document -> {
                    profileViewModel.restoreDocument(document);
                }
        );

        listView.setAdapter(trashAdapter);
        tabContentContainer.addView(listView);

        // Fetch fresh data from API
        profileViewModel.fetchDeletedDocuments();

        setListViewHeightBasedOnChildren(listView);
    }

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
        Toast.makeText(getContext(), "Đang đăng xuất...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
