package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionText, questionCounterText, timerText;
    private TextView option1Text, option2Text, option3Text, option4Text;
    private CardView option1Card, option2Card, option3Card, option4Card, questionCard;
    private Button submitNextBtn;

    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private int selectedOptionNr = 0; // 0 means nothing selected

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private DatabaseHelper dbHelper;
    private Drawable defaultOptionBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setClickListeners();

        defaultOptionBg = ContextCompat.getDrawable(this, R.drawable.option_default_bg);

        dbHelper = new DatabaseHelper(this);
        String difficulty = getIntent().getStringExtra("difficulty");

        questionList = dbHelper.getRandomQuestionsByDifficulty(difficulty, 10);
        questionCountTotal = questionList.size();

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        showNextQuestion();
    }

    private void initializeViews() {
        questionCard = findViewById(R.id.questionCard);
        questionText = findViewById(R.id.questionText);
        questionCounterText = findViewById(R.id.questionCounterText);
        timerText = findViewById(R.id.timerText);

        option1Card = findViewById(R.id.option1Card);
        option2Card = findViewById(R.id.option2Card);
        option3Card = findViewById(R.id.option3Card);
        option4Card = findViewById(R.id.option4Card);

        option1Text = findViewById(R.id.option1Text);
        option2Text = findViewById(R.id.option2Text);
        option3Text = findViewById(R.id.option3Text);
        option4Text = findViewById(R.id.option4Text);

        submitNextBtn = findViewById(R.id.submitNextBtn);
    }

    private void setClickListeners() {
        option1Card.setOnClickListener(this);
        option2Card.setOnClickListener(this);
        option3Card.setOnClickListener(this);
        option4Card.setOnClickListener(this);
        submitNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.submitNextBtn) {
            if (!answered) {
                if (selectedOptionNr == 0) {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                }
            } else {
                showNextQuestion();
            }
        } else { // An option card was clicked
            if (!answered) {
                resetOptionStyles(); // Reset previous selection
                selectedOptionNr = getOptionNrFromView(v);
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected_bg));
            }
        }
    }

    private void showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            animateAndSetQuestion();
        } else {
            finishQuiz();
        }
    }

    private void animateAndSetQuestion() {
        // Fade out animation
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(300);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // After fade out, update the content and fade in
                updateQuestionContent();
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(300);
                questionCard.startAnimation(fadeIn);
                findViewById(R.id.option1Card).startAnimation(fadeIn);
                findViewById(R.id.option2Card).startAnimation(fadeIn);
                findViewById(R.id.option3Card).startAnimation(fadeIn);
                findViewById(R.id.option4Card).startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        questionCard.startAnimation(fadeOut);
        findViewById(R.id.option1Card).startAnimation(fadeOut);
        findViewById(R.id.option2Card).startAnimation(fadeOut);
        findViewById(R.id.option3Card).startAnimation(fadeOut);
        findViewById(R.id.option4Card).startAnimation(fadeOut);
    }

    private void updateQuestionContent() {
        resetOptionStyles();
        currentQuestion = questionList.get(questionCounter);

        questionText.setText(currentQuestion.getQuestion());
        option1Text.setText("A. " + currentQuestion.getOption1());
        option2Text.setText("B. " + currentQuestion.getOption2());
        option3Text.setText("C. " + currentQuestion.getOption3());
        option4Text.setText("D. " + currentQuestion.getOption4());

        questionCounter++;
        questionCounterText.setText("Question: " + questionCounter + " / " + questionCountTotal);
        answered = false;
        selectedOptionNr = 0;
        submitNextBtn.setText("Submit");

        timeLeftInMillis = 15000;
        startCountDown();
    }

    private void resetOptionStyles() {
        option1Card.setBackground(defaultOptionBg);
        option2Card.setBackground(defaultOptionBg);
        option3Card.setBackground(defaultOptionBg);
        option4Card.setBackground(defaultOptionBg);
    }

    private int getOptionNrFromView(View v) {
        int id = v.getId();
        if (id == R.id.option1Card) return 1;
        if (id == R.id.option2Card) return 2;
        if (id == R.id.option3Card) return 3;
        if (id == R.id.option4Card) return 4;
        return 0;
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        if (selectedOptionNr == currentQuestion.getAnswerNr()) {
            score++;
        }
        showSolution();
    }

    private void showSolution() {
        resetOptionStyles(); // Clear selections first

        CardView correctCard = getCardViewFromOptionNr(currentQuestion.getAnswerNr());
        if (correctCard != null) {
            correctCard.setBackgroundColor(Color.parseColor("#4CAF50")); // Green for correct
        }

        if (selectedOptionNr != 0 && selectedOptionNr != currentQuestion.getAnswerNr()) {
            CardView selectedCard = getCardViewFromOptionNr(selectedOptionNr);
            if (selectedCard != null) {
                selectedCard.setBackgroundColor(Color.parseColor("#F44336")); // Red for incorrect
            }
        }

        if (questionCounter < questionCountTotal) {
            submitNextBtn.setText("Next Question");
        } else {
            submitNextBtn.setText("Finish Quiz");
        }
    }

    private CardView getCardViewFromOptionNr(int optionNr) {
        switch (optionNr) {
            case 1: return option1Card;
            case 2: return option2Card;
            case 3: return option3Card;
            case 4: return option4Card;
            default: return null;
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                timerText.setText( (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                checkAnswer();
            }
        }.start();
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("EXTRA_SCORE", score);
        resultIntent.putExtra("TOTAL_QUESTIONS", questionCountTotal);
        startActivity(resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}