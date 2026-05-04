package com.example.craftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegister;
    TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName      = findViewById(R.id.etName);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(v -> {
            String name     = etName.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this,
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            btnRegister.setEnabled(false);
            btnRegister.setText("Creating account...");

            RegisterRequest request = new RegisterRequest(name, email, password);

            ApiClient.getService().register(request)
                    .enqueue(new Callback<AuthResponse>() {

                        @Override
                        public void onResponse(Call<AuthResponse> call,
                                               Response<AuthResponse> response) {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Create Account");

                            if (response.isSuccessful() && response.body() != null) {
                                AuthResponse auth = response.body();

                                // ✅ Auto save token after register
                                if (auth.access != null) {
                                    SharedPreferences.Editor editor =
                                            getSharedPreferences("auth", MODE_PRIVATE).edit();
                                    editor.putString("token",   auth.access);
                                    editor.putString("refresh", auth.refresh);
                                    if (auth.user != null) {
                                        editor.putString("username", auth.user.username);
                                        editor.putString("email",    auth.user.email);
                                        editor.putInt("user_id",     auth.user.id);
                                    }
                                    editor.apply();

                                    // ✅ Go directly to Home
                                    Toast.makeText(RegisterActivity.this,
                                            "Account created! Welcome 🎉",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(
                                            RegisterActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    // No token returned → go to login
                                    Toast.makeText(RegisterActivity.this,
                                            "Account created! Please login. 🎉",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(
                                            RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }

                            } else if (response.code() == 400) {
                                Toast.makeText(RegisterActivity.this,
                                        "Username or email already exists.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "Registration failed. Try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Create Account");
                            Toast.makeText(RegisterActivity.this,
                                    "Cannot connect to server!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}