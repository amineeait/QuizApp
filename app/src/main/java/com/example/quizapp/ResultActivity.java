package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView scoreText, messageText, percentageText;
    Button restartButton, shareButton;
    ProgressBar scoreProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize all views from the layout file
        scoreText = findViewById(R.id.score_text);
        messageText = findViewById(R.id.message_text);
        percentageText = findViewById(R.id.percentage_text); // For text inside the progress circle
        restartButton = findViewById(R.id.restart_button);
        shareButton = findViewById(R.id.share_button); // The new share button
        scoreProgressBar = findViewById(R.id.score_progress_bar); // The new progress circle

        // Get the score and total questions from the Intent
        int score = getIntent().getIntExtra("EXTRA_SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 10); // Default to 10 if not provided

        // --- 1. Update UI with dynamic data ---

        // Calculate the percentage
        int percentage = (totalQuestions > 0) ? (score * 100) / totalQuestions : 0;

        // Set the dynamic score text
        scoreText.setText("You scored " + score + " out of " + totalQuestions + "!");

        // Set the progress bar and percentage text
        scoreProgressBar.setProgress(percentage);
        percentageText.setText(percentage + "%");

        // Show a custom message based on the percentage
        String message;
        if (percentage == 100) {
            message = "Perfect! You're a genius! 🏆";
        } else if (percentage >= 80) {
            message = "Excellent work! 🎉";
        } else if (percentage >= 60) {
            message = "Great job!";
        } else if (percentage >= 40) {
            message = "Not bad, keep practicing!";
        } else {
            message = "Keep trying! You can do it! 💪";
        }
        messageText.setText(message);

        // --- 2. Set up Button Listeners ---

        // Listener for the Restart Button
        restartButton.setOnClickListener(v -> {
            // Go back to the activity where users select the difficulty level
            Intent intent = new Intent(ResultActivity.this, LevelSelectActivity.class);
            startActivity(intent);
            finish(); // Finish this activity
        });

        // Listener for the new Share Button
        shareButton.setOnClickListener(v -> {
            shareScore(score, totalQuestions);
        });
    }

    /**
     * Creates and launches a share Intent to send the user's score.
     */
    private void shareScore(int score, int totalQuestions) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareMessage = "I just scored " + score + " out of " + totalQuestions + " on the Quiz App! Can you beat my score?";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share your score via"));
    }
}