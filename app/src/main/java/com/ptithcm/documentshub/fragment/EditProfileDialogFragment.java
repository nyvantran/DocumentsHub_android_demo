package com.ptithcm.documentshub.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.model.User;
import com.ptithcm.documentshub.utils.FileUtils;
import com.ptithcm.documentshub.viewmodel.ProfileViewModel;

import java.io.File;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileDialogFragment extends BottomSheetDialogFragment {

    private ImageView ivClose, ivAvatar;
    private EditText etFullName, etBio;
    private Spinner spGender;
    private Button btnCancel, btnSave;
    private ProfileViewModel profileViewModel;
    
    private boolean isInitialLoad = true;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uploadAvatar(uri);
                }
            }
    );

    public static EditProfileDialogFragment newInstance() {
        return new EditProfileDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sử dụng Activity scope để chia sẻ ViewModel
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        
        // Reset trạng thái trước khi bắt đầu phiên làm việc mới
        profileViewModel.clearUpdateResults();

        initViews(view);
        setupGenderSpinner();
        fillCurrentData();
        setupListeners();
        setupObservers();
    }

    private void fillCurrentData() {
        User user = profileViewModel.getUserProfile().getValue();
        if (user != null) {
            etFullName.setText(user.getFullName().equals("n/a") ? "" : user.getFullName());
            etBio.setText(user.getBio().equals("n/a") ? "" : user.getBio());

            String gender = user.getGender().toLowerCase();
            int position = 0;
            if (gender.equals("female")) position = 1;
            else if (gender.equals("other")) position = 2;
            spGender.setSelection(position);

            if (user.getAvatarUrl() != null) {
                Glide.with(this)
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .circleCrop()
                        .into(ivAvatar);
            }
        }
    }

    private void setupObservers() {
        profileViewModel.getUpdateProfileResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                profileViewModel.clearUpdateResults();
                profileViewModel.fetchUserProfile();
                dismiss();
            }
        });

        profileViewModel.getUpdateAvatarResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                profileViewModel.clearUpdateResults();
            }
        });

        profileViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnSave.setEnabled(!isLoading);
            btnSave.setText(isLoading ? "Đang lưu..." : "Lưu thay đổi");
        });
    }

    private void initViews(View view) {
        ivClose = view.findViewById(R.id.iv_close_dialog);
        ivAvatar = view.findViewById(R.id.iv_edit_avatar);
        etFullName = view.findViewById(R.id.et_full_name);
        etBio = view.findViewById(R.id.et_bio);
        spGender = view.findViewById(R.id.sp_gender);
        btnCancel = view.findViewById(R.id.btn_cancel_edit);
        btnSave = view.findViewById(R.id.btn_save_changes);
    }

    private void setupGenderSpinner() {
        String[] genders = {"male", "female", "other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

    private void setupListeners() {
        ivClose.setOnClickListener(v -> dismiss());
        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String bio = etBio.getText().toString().trim();
            String gender = spGender.getSelectedItem().toString().toUpperCase();

            if (name.isEmpty()) {
                etFullName.setError("Họ tên không được để trống");
                return;
            }

            profileViewModel.updateUserProfile(name, gender, null, bio);
        });

        ivAvatar.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Lắng nghe thay đổi văn bản
        TextWatcher changeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showSaveChangesButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etFullName.addTextChangedListener(changeWatcher);
        etBio.addTextChangedListener(changeWatcher);

        // Lắng nghe thay đổi Spinner
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isInitialLoad) {
                    showSaveChangesButton();
                }
                isInitialLoad = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showSaveChangesButton() {
        if (btnSave.getVisibility() != View.VISIBLE) {
            btnSave.setVisibility(View.VISIBLE);
        }
    }

    private void uploadAvatar(android.net.Uri uri) {
        File file = FileUtils.getFileFromUri(requireContext(), uri);
        if (file != null) {
            String mimeType = requireContext().getContentResolver().getType(uri);
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : "image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
            profileViewModel.updateAvatar(body);
        }
    }
}
