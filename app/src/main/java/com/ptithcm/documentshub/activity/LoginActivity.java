package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptithcm.documentshub.R;

/**
 * Activity xử lý giao diện đăng nhập.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvSignUpLink, tvShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các view từ layout
        initViews();

        // Thiết lập các sự kiện click
        setupListeners();
    }

    /**
     * Khởi tạo và ánh xạ các thành phần giao diện.
     */
    private void initViews() {
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUpLink = findViewById(R.id.tv_signup_link);
        tvShowPassword = findViewById(R.id.tv_show_password);
    }

    /**
     * Thiết lập các bộ lắng nghe sự kiện cho các thành phần UI.
     */
    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chuyển sang màn hình đăng ký", Toast.LENGTH_SHORT).show();
            }
        });

        tvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic hiển thị/ẩn mật khẩu có thể thêm ở đây
                Toast.makeText(LoginActivity.this, "Hiển thị mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Xử lý logic đăng nhập cơ bản.
     */
    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Gọi API đăng nhập qua Repository (Volley)
        Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();

        // Chuyển sang HomeActivity
        android.content.Intent intent = new android.content.Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Tắt màn hình đăng nhập
    }
}