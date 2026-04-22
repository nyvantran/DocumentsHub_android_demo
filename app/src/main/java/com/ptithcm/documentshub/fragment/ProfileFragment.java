package com.ptithcm.documentshub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.activity.LoginActivity;

public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private Button btnEditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        btnLogout = view.findViewById(R.id.btn_logout);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        setupListeners();
        
        return view;
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> {
            // Xử lý đăng xuất
            logout();
        });

        btnEditProfile.setOnClickListener(v -> {
            EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance();
            dialog.show(getChildFragmentManager(), "EditProfileDialog");
        });
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