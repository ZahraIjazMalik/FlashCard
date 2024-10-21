package com.example.flashcardintern;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AddFlashCardActivity extends AppCompatActivity {

    private EditText questionInput, answerInput, option1Input, option2Input, option3Input, option4Input;
    private Button addFlashcardButton;
    private ArrayList<FlashCard> flashcards;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_flash_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        questionInput = findViewById(R.id.questionInput);
        answerInput = findViewById(R.id.answerInput);
        option1Input = findViewById(R.id.option1Input);
        option2Input = findViewById(R.id.option2Input);
        option3Input = findViewById(R.id.option3Input);
        option4Input = findViewById(R.id.option4Input);
        addFlashcardButton = findViewById(R.id.addFlashcardButton);

        sharedPreferences = getSharedPreferences("Flashcards", MODE_PRIVATE);
        loadFlashcards();

        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionInput.getText().toString();
                String answer = answerInput.getText().toString();
                String[] options = {
                        option1Input.getText().toString(),
                        option2Input.getText().toString(),
                        option3Input.getText().toString(),
                        option4Input.getText().toString()
                };


                if (!question.isEmpty() && !answer.isEmpty() && allOptionsFilled(options)) {
                    boolean answerIsValid = false;
                    for (String option : options) {
                        if (option.equalsIgnoreCase(answer)) {
                            answerIsValid = true;
                            break;
                        }
                    }

                    if (answerIsValid) {
                        flashcards.add(new FlashCard(question, answer, options));
                        saveFlashcards();
                        Toast.makeText(AddFlashCardActivity.this, "Flashcard added!", Toast.LENGTH_SHORT).show();
                        finish(); // Return to MainActivity
                    } else {
                        Toast.makeText(AddFlashCardActivity.this, "Answer must be one of the options!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddFlashCardActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean allOptionsFilled(String[] options) {
        for (String option : options) {
            if (option.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void loadFlashcards() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("flashcardList", null);
        flashcards = gson.fromJson(json, new TypeToken<ArrayList<FlashCard>>(){}.getType());
        if (flashcards == null) {
            flashcards = new ArrayList<>();
        }
    }

    private void saveFlashcards() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(flashcards);
        editor.putString("flashcardList", json);
        editor.apply();
    }
}
