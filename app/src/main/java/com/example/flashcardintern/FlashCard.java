package com.example.flashcardintern;

import java.util.Arrays;
import java.util.List;

public class FlashCard {

    String question;
    List<String> options;
    String answer;

    public FlashCard(String question, String answer, String[] options) {
        this.question = question;
        this.options = Arrays.asList(options);
        this.answer = answer;
    }
}
