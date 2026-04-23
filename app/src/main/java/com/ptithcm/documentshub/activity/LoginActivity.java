package com.ptithcm.documentshub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.utils.TokenManager;
import com.ptithcm.documentshub.viewmodel.LoginViewModel;

/**
 * Activity xử lý giao diện đăng nhập.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvSignUpLink, tvShowPassword;
    private LoginViewModel viewModel;
    private TokenManager tokenManager;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager(this);
        
        // Kiểm tra nếu đã có token thì chuyển thẳng vào Home (tùy chọn)
        if (tokenManager.getAccessToken() != null) {
            // startHomeActivity();
        }

        initViews();
        setupViewModel();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUpLink = findViewById(R.id.tv_signup_link);
        tvShowPassword = findViewById(R.id.tv_show_password);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Quan sát trạng thái Loading
        viewModel.getIsLoading().observe(this, isLoading -> {
            btnLogin.setEnabled(!isLoading);
            btnLogin.setText(isLoading ? "Đang xử lý..." : getString(R.string.btn_login));
        });

        // Quan sát kết quả đăng nhập
        viewModel.getLoginResult().observe(this, loginResponse -> {
            if (loginResponse != null) {
                // Lưu token
                tokenManager.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                
                // Chuyển sang HomeActivity
                startHomeActivity();
            }
        });

        // Quan sát lỗi
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            String identity = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.login(identity, password);
        });

        tvShowPassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                etPassword.setTransformationMethod(null);
                tvShowPassword.setText("Ẩn");
            } else {
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                tvShowPassword.setText(getString(R.string.show_password));
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        tvSignUpLink.setOnClickListener(v -> {
            // Chuyển sang RegisterActivity (nếu có)
            Toast.makeText(LoginActivity.this, "Chuyển sang màn hình đăng ký", Toast.LENGTH_SHORT).show();
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}