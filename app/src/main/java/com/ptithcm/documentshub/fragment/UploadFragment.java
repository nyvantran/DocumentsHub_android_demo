package com.ptithcm.documentshub.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.Category;
import com.ptithcm.documentshub.viewmodel.ProfileViewModel;
import com.ptithcm.documentshub.viewmodel.UploadViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment xử lý giao diện upload tài liệu.
 * Kết nối với UploadViewModel để gọi API upload và load categories.
 */
public class UploadFragment extends Fragment {

    private EditText etTitle, etDescription, etTags;
    private Button btnSelectFile, btnUpload;
    private TextView tvSelectedFileName;
    private Spinner spVisibility, spCategory;
    private LinearLayout lnTagsContainer;
    private FrameLayout flLoadingOverlay;

    private List<String> tagList = new ArrayList<>();
    private Uri selectedFileUri;

    // Danh sách categories từ API, dùng để lấy category_id khi upload
    private List<Category> categoryList = new ArrayList<>();

    // ViewModel xử lý logic upload và load categories
    private UploadViewModel uploadViewModel;

    // Launcher để chọn file từ hệ thống
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        String fileName = getFileName(selectedFileUri);
                        tvSelectedFileName.setText(fileName);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        initViews(view);
        setupViewModel();
        setupSpinners();
        setupListeners();
        return view;
    }

    /**
     * Khởi tạo các view từ layout.
     */
    private void initViews(View view) {
        etTitle = view.findViewById(R.id.et_document_title);
        etDescription = view.findViewById(R.id.et_document_description);
        etTags = view.findViewById(R.id.et_tags);
        btnSelectFile = view.findViewById(R.id.btn_select_file);
        btnUpload = view.findViewById(R.id.btn_upload);
        tvSelectedFileName = view.findViewById(R.id.tv_selected_file_name);
        spVisibility = view.findViewById(R.id.sp_visibility);
        spCategory = view.findViewById(R.id.sp_category);
        lnTagsContainer = view.findViewById(R.id.ln_tags_container);
        flLoadingOverlay = view.findViewById(R.id.fl_loading_overlay);
    }

    /**
     * Khởi tạo ViewModel và observe các LiveData.
     */
    private void setupViewModel() {
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);

        // Observe danh sách categories từ API
        uploadViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryList = categories;
                List<String> categoryNames = new ArrayList<>();
                for (Category cat : categories) {
                    categoryNames.add(cat.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategory.setAdapter(adapter);
            }
        });

        // Observe trạng thái loading
        uploadViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                flLoadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnUpload.setEnabled(!isLoading);
            }
        });

        // Observe kết quả upload
        uploadViewModel.getUploadResult().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), R.string.msg_upload_success, Toast.LENGTH_LONG).show();
                resetForm();

                // Refresh danh sách documents ở ProfileFragment
                // Sử dụng ProfileViewModel scope Activity để chia sẻ dữ liệu giữa các Fragment
                if (requireActivity() != null) {
                    ProfileViewModel profileViewModel = new ViewModelProvider(requireActivity())
                            .get(ProfileViewModel.class);
                    profileViewModel.fetchReadyDocuments();
                }
            }
        });

        // Observe thông báo lỗi
        uploadViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Tải danh sách categories từ API
        uploadViewModel.fetchCategories();
    }

    /**
     * Thiết lập các Spinner với dữ liệu mặc định.
     * Category Spinner sẽ được cập nhật khi API trả về dữ liệu.
     */
    private void setupSpinners() {
        // Visibility Spinner: chỉ có PUBLIC và PRIVATE (khớp với backend)
        String[] visibilityOptions = {"Public", "Private"};
        ArrayAdapter<String> visibilityAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                visibilityOptions
        );
        visibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVisibility.setAdapter(visibilityAdapter);

        // Category Spinner: hiển thị "Loading..." cho đến khi API trả về
        String[] loadingOptions = {"Loading..."};
        ArrayAdapter<String> loadingAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                loadingOptions
        );
        loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(loadingAdapter);
    }

    /**
     * Thiết lập các event listeners cho buttons và input fields.
     */
    private void setupListeners() {
        // Mở trình chọn file của hệ thống
        btnSelectFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        // Nút Upload: validate và gọi API upload
        btnUpload.setOnClickListener(v -> handleUpload());

        // Xử lý thêm tag khi nhấn Enter
        etTags.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                addTag();
                return true;
            }
            return false;
        });

        // Xử lý thêm tag khi nhập dấu cách hoặc dấu phẩy
        etTags.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.endsWith(" ") || str.endsWith(",")) {
                    addTag();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    /**
     * Xử lý logic validate và gọi upload.
     */
    private void handleUpload() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate title
        if (title.isEmpty()) {
            etTitle.setError(getString(R.string.msg_title_required));
            return;
        }

        // Validate file
        if (selectedFileUri == null) {
            Toast.makeText(getContext(), R.string.msg_select_file_first, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate category đã load từ API
        if (categoryList.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_load_categories_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy category_id từ Spinner position
        int selectedCategoryPosition = spCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedCategoryPosition).getId();

        // Lấy visibility từ Spinner
        String visibility = spVisibility.getSelectedItemPosition() == 0 ? "PUBLIC" : "PRIVATE";

        // Gọi ViewModel để upload
        uploadViewModel.uploadDocument(
                selectedFileUri,
                title,
                categoryId,
                visibility,
                description.isEmpty() ? null : description,
                tagList,
                requireContext().getContentResolver()
        );
    }

    /**
     * Reset form sau khi upload thành công.
     */
    private void resetForm() {
        etTitle.setText("");
        etDescription.setText("");
        etTags.setText("");
        selectedFileUri = null;
        tvSelectedFileName.setText(R.string.no_file_selected);
        spVisibility.setSelection(0);
        spCategory.setSelection(0);
        tagList.clear();
        lnTagsContainer.removeAllViews();
    }

    /**
     * Thêm tag từ ô nhập liệu vào danh sách.
     */
    private void addTag() {
        String tag = etTags.getText().toString().trim().replace(",", "");
        if (!tag.isEmpty() && !tagList.contains(tag)) {
            tagList.add(tag);
            createTagView(tag);
            etTags.setText("");
        }
    }

    /**
     * Tạo view hiển thị tag với nút xóa.
     */
    private void createTagView(String tag) {
        View tagView = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, lnTagsContainer, false);
        TextView tvTagName = tagView.findViewById(R.id.tv_tag_name);
        ImageView ivRemoveTag = tagView.findViewById(R.id.iv_remove_tag);

        tvTagName.setText(tag);
        ivRemoveTag.setOnClickListener(v -> {
            lnTagsContainer.removeView(tagView);
            tagList.remove(tag);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 8, 0);
        tagView.setLayoutParams(params);

        lnTagsContainer.addView(tagView);
    }

    /**
     * Lấy tên file từ Uri thông qua ContentResolver.
     */
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}