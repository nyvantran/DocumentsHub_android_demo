package com.ptithcm.documentshub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptithcm.documentshub.R;

public class SignUpOTPActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etEmail;
    private EditText etOtpCode;
    private Button btnNext;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_otp);

        email = getIntent().getStringExtra("email");

        initViews();
        setupListeners();
        
        if (email != null) {
            etEmail.setText(email);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etEmail = findViewById(R.id.et_otp_email);
        etOtpCode = findViewById(R.id.et_otp_code);
        btnNext = findViewById(R.id.btn_otp_next);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String otp = etOtpCode.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang màn hình nhập Username/Password
            Intent intent = new Intent(SignUpOTPActivity.this, SignUpUserPasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}