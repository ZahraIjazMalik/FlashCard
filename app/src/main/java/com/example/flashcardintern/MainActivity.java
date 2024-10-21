package com.example.flashcardintern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button addFlashcardButton, viewFlashcardsButton, startQuizButton;
    private ArrayList<FlashCard> flashcards;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addFlashcardButton = findViewById(R.id.addflashcardButton); // Correct ID
        viewFlashcardsButton = findViewById(R.id.viewflashcardsButton);
        startQuizButton = findViewById(R.id.startquizButton); // Correct ID

        sharedPreferences = getSharedPreferences("Flashcards", MODE_PRIVATE);
        loadFlashcards();

        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFlashCardActivity.class);
                startActivity(intent);

            }
        });

        viewFlashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewFlashCardActivity.class);
                startActivity(intent);
            }
        });

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
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

}