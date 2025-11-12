package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomePageLoads() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("Welcome to...test server")));
    }

    @Test
    void testGetQuestionPageLoads() throws Exception {
        mockMvc.perform(get("/get_question"))
            .andExpect(status().isOk())
            .andExpect(view().name("question"))
            .andExpect(model().attributeExists("count"))
            .andExpect(model().attributeExists("myString"))
            .andExpect(model().attributeExists("score"));
    }

    @Test
    void testQuestionSubmission() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        // First, get a question
        mockMvc.perform(get("/get_question").session(session))
            .andExpect(status().isOk());
        
        // Then submit an answer
        mockMvc.perform(post("/get_question")
                .param("answer", "true")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("question"))
            .andExpect(model().attributeExists("isCorrect"));
    }

    @Test
    void testSessionIndependence() throws Exception {
        // Create two separate sessions
        MockHttpSession session1 = new MockHttpSession();
        MockHttpSession session2 = new MockHttpSession();
        
        // Session 1: Get first question
        mockMvc.perform(get("/get_question").session(session1))
            .andExpect(status().isOk())
            .andExpect(model().attribute("count", hasProperty("count", is(0))));
        
        // Session 1: Submit answer
        mockMvc.perform(post("/get_question")
                .param("answer", "true")
                .session(session1))
            .andExpect(status().isOk())
            .andExpect(model().attribute("count", hasProperty("count", is(1))));
        
        // Session 2: Get first question (should start at 0, not 1)
        mockMvc.perform(get("/get_question").session(session2))
            .andExpect(status().isOk())
            .andExpect(model().attribute("count", hasProperty("count", is(0))));
    }

    @Test
    void testScoreTracking() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        // Get first question
        mockMvc.perform(get("/get_question").session(session))
            .andExpect(status().isOk())
            .andExpect(model().attribute("score", is(0)));
        
        // Submit answer
        mockMvc.perform(post("/get_question")
                .param("answer", "true")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("score"));
    }

    @Test
    void testMultipleQuestionsInSequence() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        // Answer 3 questions in sequence
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/get_question").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("count", hasProperty("count", is(i))));
            
            mockMvc.perform(post("/get_question")
                    .param("answer", "true")
                    .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("count", hasProperty("count", is(i + 1))));
        }
    }

    @Test
    void testResetFunctionality() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        // Get and answer a question
        mockMvc.perform(get("/get_question").session(session))
            .andExpect(status().isOk());
        
        mockMvc.perform(post("/get_question")
                .param("answer", "true")
                .session(session))
            .andExpect(status().isOk());
        
        // Reset
        mockMvc.perform(get("/reset").session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/get_question"));
    }

    @Test
    void testGreetingFormSubmission() throws Exception {
        mockMvc.perform(get("/greeting"))
            .andExpect(status().isOk())
            .andExpect(view().name("greeting"))
            .andExpect(model().attributeExists("greeting"));
        
        mockMvc.perform(post("/greeting")
                .param("id", "123")
                .param("content", "Test greeting"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"));
    }

    @Test
    void testBothUrlFormats() throws Exception {
        // Test both /get_question and /get-question work
        mockMvc.perform(get("/get_question"))
            .andExpect(status().isOk());
        
        mockMvc.perform(get("/get-question"))
            .andExpect(status().isOk());
    }

    @Test
    void testAnswerValidation() throws Exception {
        MockHttpSession session = new MockHttpSession();
        
        // Get question
        mockMvc.perform(get("/get_question").session(session))
            .andExpect(status().isOk());
        
        // Test with true answer
        mockMvc.perform(post("/get_question")
                .param("answer", "true")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("isCorrect"));
        
        // Test with false answer
        mockMvc.perform(post("/get_question")
                .param("answer", "false")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("isCorrect"));
    }
}