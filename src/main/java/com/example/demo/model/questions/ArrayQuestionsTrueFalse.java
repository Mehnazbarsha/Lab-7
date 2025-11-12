package com.example.demo.model.questions;

import java.util.ArrayList;
import java.util.List;

public class ArrayQuestionsTrueFalse {
    private final List<QuestionTrueFalse> arrayListQuestionsTF;
    private final int totalQuestions;

    public ArrayQuestionsTrueFalse() {
        this.arrayListQuestionsTF = new ArrayList<>();
        arrayListQuestionsTF.add(new QuestionTrueFalse("The earth is the 3rd planet from its star", true));
        arrayListQuestionsTF.add(new QuestionTrueFalse("The earth and mars have the same atmosphere", false));
        arrayListQuestionsTF.add(new QuestionTrueFalse("Saturn in the largest planet", false));
        arrayListQuestionsTF.add(new QuestionTrueFalse("Jupiter in the largest planet", true));
        arrayListQuestionsTF.add(new QuestionTrueFalse("No planets are retrograde in our solar system", false));
        this.totalQuestions = arrayListQuestionsTF.size();
    }

    public QuestionTrueFalse nextQuestion(int i) {
        if (totalQuestions == 0) {
            throw new IllegalStateException("No questions available");
        }
        int idx = i % totalQuestions;
        if (idx < 0) {
            idx += totalQuestions; // normalize negative modulo to valid index
        }
        return arrayListQuestionsTF.get(idx);
    }
    
}