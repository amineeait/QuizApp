package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectActivity extends AppCompatActivity {

    Button easyBtn, mediumBtn, hardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        easyBtn = findViewById(R.id.easyBtn);
        mediumBtn = findViewById(R.id.mediumBtn);
        hardBtn = findViewById(R.id.hardBtn);

        easyBtn.setOnClickListener(v -> {
            // Start quiz activity with "easy" difficulty
            Intent intent = new Intent(LevelSelectActivity.this, MainActivity.class);
            intent.putExtra("difficulty", "easy");
            startActivity(intent);
            finish();
        });

        mediumBtn.setOnClickListener(v -> {
            // Start quiz activity with "medium" difficulty
            Intent intent = new Intent(LevelSelectActivity.this, MainActivity.class);
            intent.putExtra("difficulty", "medium");
            startActivity(intent);
            finish();
        });

        hardBtn.setOnClickListener(v -> {
            // Start quiz activity with "hard" difficulty
            Intent intent = new Intent(LevelSelectActivity.this, MainActivity.class);
            intent.putExtra("difficulty", "hard");
            startActivity(intent);
            finish();
        });
    }
}
