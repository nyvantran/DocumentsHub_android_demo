package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptithcm.documentshub.R;

public class SignUpUserPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etEmail, etUsername, etPassword;
    private TextView tvShowPassword;
    private Button btnFinish;
    private String email;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user_password);

        email = getIntent().getStringExtra("email");

        initViews();
        setupListeners();

        if (email != null) {
            etEmail.setText(email);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etEmail = findViewById(R.id.et_register_email);
        etUsername = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_password);
        tvShowPassword = findViewById(R.id.tv_show_password);
        btnFinish = findViewById(R.id.btn_register_finish);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

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

        btnFinish.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Gọi API đăng ký chính thức
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            
            // Có thể chuyển về màn hình đăng nhập
            finish();
        });
    }
}