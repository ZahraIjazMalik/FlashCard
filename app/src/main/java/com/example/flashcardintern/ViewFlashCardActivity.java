package com.example.flashcardintern;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewFlashCardActivity extends AppCompatActivity {

    private TextView flashcardsTextView;
    private ArrayList<FlashCard> flashcards;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_flash_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flashcardsTextView = findViewById(R.id.flashcardsTextView);
        sharedPreferences = getSharedPreferences("Flashcards", MODE_PRIVATE);
        loadFlashcards();
        displayFlashcards();
    }

    private void loadFlashcards() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("flashcardList", null);
        Type type = new TypeToken<ArrayList<FlashCard>>() {
        }.getType();
        flashcards = gson.fromJson(json, type);
        if (flashcards == null) {
            flashcards = new ArrayList<>();
        }
    }

    private void displayFlashcards() {
        StringBuilder stringBuilder = new StringBuilder();
        for (FlashCard flashcard : flashcards) {
            stringBuilder.append("Q: ").append(flashcard.question).append("\nA: ").append(flashcard.answer).append("\n\n");
        }
        flashcardsTextView.setText(stringBuilder.toString());
    }
}