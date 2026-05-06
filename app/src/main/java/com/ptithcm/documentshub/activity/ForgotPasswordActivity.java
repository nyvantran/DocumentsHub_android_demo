package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.viewmodel.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ConstraintLayout clStep1, clStep2;
    private EditText etIdentity, etOtp, etNewPassword;
    private Button btnSendOtp, btnResetPassword;
    private TextView tvBackToLogin;
    private ForgotPasswordViewModel viewModel;
    private String identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setupViewModel();
        setupListeners();
    }

    private void initViews() {
        clStep1 = findViewById(R.id.cl_step_1);
        clStep2 = findViewById(R.id.cl_step_2);
        etIdentity = findViewById(R.id.et_forgot_identity);
        etOtp = findViewById(R.id.et_reset_otp);
        etNewPassword = findViewById(R.id.et_reset_new_password);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        tvBackToLogin = findViewById(R.id.tv_back_to_login);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        viewModel.getForgotPasswordResponse().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                clStep1.setVisibility(View.GONE);
                clStep2.setVisibility(View.VISIBLE);
                Toast.makeText(this, "OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getResetPasswordResponse().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, getString(R.string.reset_password_success), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSendOtp.setEnabled(!isLoading);
            btnResetPassword.setEnabled(!isLoading);
        });
    }

    private void setupListeners() {
        btnSendOtp.setOnClickListener(v -> {
            identity = etIdentity.getText().toString().trim();
            if (identity.isEmpty()) {
                etIdentity.setError("Vui lòng nhập email hoặc username");
                return;
            }
            viewModel.forgotPassword(identity);
        });

        btnResetPassword.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();

            if (otp.isEmpty()) {
                etOtp.setError("Vui lòng nhập mã OTP");
                return;
            }
            if (newPassword.isEmpty() || newPassword.length() < 8) {
                etNewPassword.setError("Mật khẩu phải từ 8 ký tự");
                return;
            }

            viewModel.resetPassword(identity, otp, newPassword);
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }
}
