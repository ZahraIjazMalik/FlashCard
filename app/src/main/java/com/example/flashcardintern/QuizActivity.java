package com.example.flashcardintern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private ArrayList<FlashCard> flashcards;
    private TextView questionText, quest1;
    private RadioGroup optionsGroup;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quest1 = findViewById(R.id.ques1);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsRadioGroup);

        loadFlashcards();

        if (flashcards.isEmpty()) {
            Toast.makeText(this, "No flashcards available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            displayQuestion();
        }

        optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkAnswer(checkedId);
            }
        });
    }

    private void loadFlashcards() {
        SharedPreferences sharedPreferences = getSharedPreferences("Flashcards", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("flashcardList", null);
        Type type = new TypeToken<ArrayList<FlashCard>>() {
        }.getType();
        flashcards = gson.fromJson(json, type);
        if (flashcards == null) {
            flashcards = new ArrayList<>();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < flashcards.size()) {
            FlashCard flashcard = flashcards.get(currentQuestionIndex);
            quest1.setText("Q" + (currentQuestionIndex + 1) + ":");
            questionText.setText(flashcard.question);
            optionsGroup.removeAllViews();

            for (String option : flashcard.options) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                optionsGroup.addView(radioButton);
            }
        } else {

            Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        }
    }

    private void checkAnswer(int checkedId) {
        if (optionsGroup.getChildCount() == 0) return;

        RadioButton selectedRadioButton = findViewById(checkedId);
        if (selectedRadioButton == null) {
            Toast.makeText(this, "Error: No selected option!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userAnswer = selectedRadioButton.getText().toString();
        FlashCard flashcard = flashcards.get(currentQuestionIndex);


        for (int i = 0; i < optionsGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
            if (radioButton != null) {
                radioButton.setTextColor(getResources().getColor(android.R.color.black));
            }
        }


        if (userAnswer.equalsIgnoreCase(flashcard.answer)) {
            score++;
            selectedRadioButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            selectedRadioButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));


            for (int i = 0; i < optionsGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
                if (radioButton != null && radioButton.getText().toString().equalsIgnoreCase(flashcard.answer)) {
                    radioButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                }
            }
        }

        optionsGroup.setEnabled(false);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                displayQuestion();
                optionsGroup.setEnabled(true);
            }
        }, 1000);
    }
}
