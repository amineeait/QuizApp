package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameInput, passwordInput, confirmPasswordInput;
    private MaterialButton registerButton;
    private TextView loginLinkText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // (Initialization code is the same)
        usernameInput = findViewById(R.id.registerUsernameInput);
        passwordInput = findViewById(R.id.registerPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        loginLinkText = findViewById(R.id.loginLinkText);
        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> performRegistration());

        loginLinkText.setOnClickListener(v -> {
            // Simply finish this activity to go back to the Login screen
            finish();
        });
    }

    private void performRegistration() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // (Validation logic is the same)
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.insertUser(username, password);

        if (isInserted) {
            Toast.makeText(this, "Account created successfully! Logging in...", Toast.LENGTH_SHORT).show();

            // *** IMPORTANT: Go directly to the Level Select screen ***
            // This provides a better user experience than making them log in again.
            Intent intent = new Intent(RegisterActivity.this, LevelSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish(); // Finish this activity and the one behind it (Login)
        } else {
            Toast.makeText(this, "Registration failed. Username might already exist.", Toast.LENGTH_LONG).show();
        }
    }
}