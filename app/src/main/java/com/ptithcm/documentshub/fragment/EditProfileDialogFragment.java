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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ptithcm.documentshub.R;

public class EditProfileDialogFragment extends BottomSheetDialogFragment {

    private ImageView ivClose, ivAvatar;
    private EditText etFullName, etBio;
    private Spinner spGender;
    private Button btnCancel, btnSave;
    
    private boolean isInitialLoad = true;

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

        initViews(view);
        setupGenderSpinner();
        setupListeners();
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
            if (name.isEmpty()) {
                etFullName.setError("Name is required");
                return;
            }
            Toast.makeText(getContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        ivAvatar.setOnClickListener(v -> {
            showSaveChangesButton();
            Toast.makeText(getContext(), "Change avatar functionality coming soon", Toast.LENGTH_SHORT).show();
        });

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
}