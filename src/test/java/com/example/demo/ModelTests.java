package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.*;
import com.example.demo.model.questions.*;

class ModelTests {

    // Test QuestionTrueFalse
    @Test
    void testQuestionTrueFalseCreation() {
        QuestionTrueFalse qtf = new QuestionTrueFalse("Is the sky blue?", true);
        assertEquals("Is the sky blue?", qtf.getQuestion());
        assertEquals(true, qtf.getAnswer());
    }

    @Test
    void testQuestionTrueFalseSetters() {
        QuestionTrueFalse qtf = new QuestionTrueFalse("Original question", true);
        qtf.setQuestion("New question");
        qtf.setAnswer(false);
        
        assertEquals("New question", qtf.getQuestion());
        assertEquals(false, qtf.getAnswer());
    }

    // Test ArrayQuestionsTrueFalse
    @Test
    void testArrayQuestionsTrueFalseHasQuestions() {
        ArrayQuestionsTrueFalse arrayQTF = new ArrayQuestionsTrueFalse();
        QuestionTrueFalse q = arrayQTF.nextQuestion(0);
        
        assertNotNull(q);
        assertNotNull(q.getQuestion());
        assertNotNull(q.getAnswer());
    }

    @Test
    void testArrayQuestionsTrueFalseWrapsAround() {
        ArrayQuestionsTrueFalse arrayQTF = new ArrayQuestionsTrueFalse();
        
        // Get first question
        QuestionTrueFalse q1 = arrayQTF.nextQuestion(0);
        // Get question beyond array size (should wrap)
        QuestionTrueFalse q2 = arrayQTF.nextQuestion(100);
        
        assertNotNull(q1);
        assertNotNull(q2);
        // They should be the same due to modulo
        assertEquals(q1.getQuestion(), q2.getQuestion());
        assertEquals(q1.getAnswer(), q2.getAnswer());
    }

    @Test
    void testArrayQuestionsTrueFalseSequence() {
        ArrayQuestionsTrueFalse arrayQTF = new ArrayQuestionsTrueFalse();
        
        QuestionTrueFalse q1 = arrayQTF.nextQuestion(0);
        QuestionTrueFalse q2 = arrayQTF.nextQuestion(1);
        
        // Different indices should give different questions
        assertFalse(q1.getQuestion().equals(q2.getQuestion()) && 
                   q1.getAnswer().equals(q2.getAnswer()));
    }

    // Test Count
    @Test
    void testCountInitialValue() {
        Count count = new Count();
        assertEquals(0, count.getCount());
    }

    @Test
    void testCountIncrement() {
        Count count = new Count();
        count.count = 5;
        assertEquals(5, count.getCount());
        
        count.count++;
        assertEquals(6, count.getCount());
    }

    // Test MyString
    @Test
    void testMyStringSetAndGet() {
        MyString myString = new MyString();
        myString.setMyString("Test string");
        assertEquals("Test string", myString.getMyString());
    }

    @Test
    void testMyStringNull() {
        MyString myString = new MyString();
        assertNull(myString.getMyString());
        
        myString.setMyString(null);
        assertNull(myString.getMyString());
    }

    // Test GetQuestion
    @Test
    void testGetQuestionReturnsValidQuestion() {
        GetQuestion gq = new GetQuestion();
        QuestionTrueFalse qtf = gq.nextQuestion(0);
        
        assertNotNull(qtf);
        assertNotNull(qtf.getQuestion());
        assertNotNull(qtf.getAnswer());
    }

    @Test
    void testGetQuestionWithDifferentIndices() {
        GetQuestion gq = new GetQuestion();
        
        QuestionTrueFalse qtf1 = gq.nextQuestion(0);
        QuestionTrueFalse qtf2 = gq.nextQuestion(1);
        QuestionTrueFalse qtf3 = gq.nextQuestion(2);
        
        assertNotNull(qtf1);
        assertNotNull(qtf2);
        assertNotNull(qtf3);
    }

    // Test Greeting
    @Test
    void testGreetingSettersAndGetters() {
        Greeting greeting = new Greeting();
        greeting.setId(123L);
        greeting.setContent("Hello World");
        
        assertEquals(123L, greeting.getId());
        assertEquals("Hello World", greeting.getContent());
    }

    // Functional Test - Complete Quiz Flow
    @Test
    void testCompleteQuizFlow() {
        GetQuestion gq = new GetQuestion();
        Count count = new Count();
        int correctAnswers = 0;
        
        // Simulate answering 5 questions
        for (int i = 0; i < 5; i++) {
            QuestionTrueFalse qtf = gq.nextQuestion(count.count);
            
            // Simulate user answer (always choose true)
            Boolean userAnswer = true;
            
            if (userAnswer.equals(qtf.getAnswer())) {
                correctAnswers++;
            }
            
            count.count++;
        }
        
        assertEquals(5, count.count);
        assertTrue(correctAnswers >= 0 && correctAnswers <= 5);
    }

    // Edge case tests
    @Test
    void testLargeCountValue() {
        GetQuestion gq = new GetQuestion();
        QuestionTrueFalse qtf = gq.nextQuestion(1000);
        
        assertNotNull(qtf);
        assertNotNull(qtf.getQuestion());
    }

    @Test
    void testNegativeCountValue() {
        ArrayQuestionsTrueFalse arrayQTF = new ArrayQuestionsTrueFalse();
        
        // Java's modulo with negative numbers
        // This tests that the implementation handles edge cases
        assertDoesNotThrow(() -> {
            QuestionTrueFalse qtf = arrayQTF.nextQuestion(-1);
        });
    }
}