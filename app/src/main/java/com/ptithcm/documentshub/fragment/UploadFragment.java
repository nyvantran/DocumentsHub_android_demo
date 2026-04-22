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

import com.ptithcm.documentshub.R;

import java.util.ArrayList;
import java.util.List;

public class UploadFragment extends Fragment {

    private EditText etTitle, etDescription, etTags;
    private Button btnSelectFile, btnUpload;
    private TextView tvSelectedFileName;
    private Spinner spVisibility, spCategory;
    private LinearLayout lnTagsContainer;
//    private ImageView ivBack;

    private List<String> tagList = new ArrayList<>();
    private Uri selectedFileUri;

    // Khai báo launcher để chọn file
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
        setupSpinners();
        setupListeners();
        return view;
    }

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
//        ivBack = view.findViewById(R.id.iv_back);
    }

    private void setupSpinners() {
        // Visibility Spinner
        String[] visibilityOptions = {"Public", "Private", "Unlisted"};
        ArrayAdapter<String> visibilityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, visibilityOptions);
        visibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVisibility.setAdapter(visibilityAdapter);

        // Category Spinner
        String[] categoryOptions = {"Programming", "Computer", "Science", "Math", "History"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        btnSelectFile.setOnClickListener(v -> {
            // Mở trình chọn file của hệ thống
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Cho phép chọn mọi loại file
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        btnUpload.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                return;
            }
            if (selectedFileUri == null) {
                Toast.makeText(getContext(), "Please select a file first", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Uploading: " + title, Toast.LENGTH_LONG).show();
        });

        etTags.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                addTag();
                return true;
            }
            return false;
        });

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

//        ivBack.setOnClickListener(v -> {
//            if (getActivity() != null) {
//                getActivity().onBackPressed();
//            }
//        });
    }

    private void addTag() {
        String tag = etTags.getText().toString().trim().replace(",", "");
        if (!tag.isEmpty() && !tagList.contains(tag)) {
            tagList.add(tag);
            createTagView(tag);
            etTags.setText("");
        }
    }

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

    // Hàm lấy tên file từ Uri
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