package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    Button easyBtn, mediumBtn, hardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        easyBtn = findViewById(R.id.easyBtn);
        mediumBtn = findViewById(R.id.mediumBtn);
        hardBtn = findViewById(R.id.hardBtn);

        easyBtn.setOnClickListener(v -> startQuiz("easy"));
        mediumBtn.setOnClickListener(v -> startQuiz("medium"));
        hardBtn.setOnClickListener(v -> startQuiz("hard"));
    }

    private void startQuiz(String difficulty) {
        Intent intent = new Intent(DifficultyActivity.this, MainActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}
