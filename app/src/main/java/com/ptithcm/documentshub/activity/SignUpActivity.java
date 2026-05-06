package com.ptithcm.documentshub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.utils.TokenManager;
import com.ptithcm.documentshub.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ConstraintLayout clStep1, clStep2, clStep3;
    private View step1Indicator, step2Indicator, step3Indicator;
    private EditText etEmail, etOtp, etUsername, etPassword;
    private Button btnNext1, btnNext2, btnFinish;
    private TextView tvLoginLink, tvShowPassword;
    
    private SignUpViewModel viewModel;
    private String email, registrationCode;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        setupViewModel();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        
        clStep1 = findViewById(R.id.cl_step_1);
        clStep2 = findViewById(R.id.cl_step_2);
        clStep3 = findViewById(R.id.cl_step_3);
        
        step1Indicator = findViewById(R.id.step_1_indicator);
        step2Indicator = findViewById(R.id.step_2_indicator);
        step3Indicator = findViewById(R.id.step_3_indicator);
        
        etEmail = findViewById(R.id.et_register_email);
        etOtp = findViewById(R.id.et_otp_code);
        etUsername = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_password);
        
        btnNext1 = findViewById(R.id.btn_register_next);
        btnNext2 = findViewById(R.id.btn_otp_next);
        btnFinish = findViewById(R.id.btn_register_finish);
        
        tvLoginLink = findViewById(R.id.tv_login_link);
        tvShowPassword = findViewById(R.id.tv_show_password_reg);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        // Step 1 Response
        viewModel.getRegisterRequestResponse().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                moveToStep2();
            }
        });

        // Step 2 Response
        viewModel.getRegisterVerifyResponse().observe(this, response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                registrationCode = response.getData().getRegistrationCode();
                moveToStep3();
            }
        });

        // Step 3 Response
        viewModel.getRegisterCompleteResponse().observe(this, response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                TokenManager.getInstance(this).saveTokens(
                        response.getData().getAccessToken(),
                        response.getData().getRefreshToken()
                );
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                finishAffinity();
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnNext1.setEnabled(!isLoading);
            btnNext2.setEnabled(!isLoading);
            btnFinish.setEnabled(!isLoading);
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> handleBack());

        btnNext1.setOnClickListener(v -> {
            email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Vui lòng nhập email");
                return;
            }
            viewModel.registerRequest(email);
        });

        btnNext2.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                etOtp.setError("Vui lòng nhập mã OTP");
                return;
            }
            viewModel.registerVerify(email, otp);
        });

        btnFinish.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.isEmpty()) {
                etUsername.setError("Vui lòng nhập username");
                return;
            }
            if (password.length() < 8) {
                etPassword.setError("Mật khẩu phải ít nhất 8 ký tự");
                return;
            }
            viewModel.registerComplete(email, registrationCode, username, password);
        });

        tvLoginLink.setOnClickListener(v -> finish());

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
    }

    private void handleBack() {
        if (clStep2.getVisibility() == View.VISIBLE) {
            moveToStep1();
        } else if (clStep3.getVisibility() == View.VISIBLE) {
            moveToStep2();
        } else {
            finish();
        }
    }

    private void moveToStep1() {
        clStep1.setVisibility(View.VISIBLE);
        clStep2.setVisibility(View.GONE);
        clStep3.setVisibility(View.GONE);
        updateIndicators(1);
    }

    private void moveToStep2() {
        clStep1.setVisibility(View.GONE);
        clStep2.setVisibility(View.VISIBLE);
        clStep3.setVisibility(View.GONE);
        updateIndicators(2);
    }

    private void moveToStep3() {
        clStep1.setVisibility(View.GONE);
        clStep2.setVisibility(View.GONE);
        clStep3.setVisibility(View.VISIBLE);
        updateIndicators(3);
    }

    private void updateIndicators(int step) {
        step1Indicator.setBackgroundResource(step == 1 ? R.drawable.bg_step_dot_active : R.drawable.bg_step_dot_inactive);
        step2Indicator.setBackgroundResource(step == 2 ? R.drawable.bg_step_dot_active : R.drawable.bg_step_dot_inactive);
        step3Indicator.setBackgroundResource(step == 3 ? R.drawable.bg_step_dot_active : R.drawable.bg_step_dot_inactive);
        
        // Cập nhật độ dài (width) cho active indicator nếu cần (tùy thuộc vào drawable)
        // Trong XML đã set width cố định, nếu muốn giống layout cũ thì cần setLayoutParams
        setIndicatorWidth(step1Indicator, step == 1);
        setIndicatorWidth(step2Indicator, step == 2);
        setIndicatorWidth(step3Indicator, step == 3);
    }

    private void setIndicatorWidth(View view, boolean isActive) {
        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = isActive ? dpToPx(30) : dpToPx(8);
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }
}
