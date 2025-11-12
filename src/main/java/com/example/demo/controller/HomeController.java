package com.example.demo.controller;

import com.example.demo.model.Count;
import com.example.demo.model.Greeting;
import com.example.demo.model.MyString;
import com.example.demo.model.GetQuestion;
import com.example.demo.model.questions.QuestionTrueFalse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model, HttpSession session) {
        Count count = (Count) session.getAttribute("count");
        if (count == null) {
            count = new Count();
            session.setAttribute("count", count);
        }
        count.count = count.count + 1;
        session.setAttribute("count", count);
        
        model.addAttribute("greeting", new Greeting());
        model.addAttribute("count", count);
        
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, 
                                 Model model, 
                                 HttpSession session) {
        // ensure non-null Count in session and model
        Count count = (Count) session.getAttribute("count");
        if (count == null) {
            count = new Count();
            session.setAttribute("count", count);
        }
        model.addAttribute("greeting", greeting);
        model.addAttribute("count", count);
        return "result";
      }

    @GetMapping({"/get_question", "/get-question"})
    public String questionForm(Model model, HttpSession session) {
        // Get or create count for this session
        Count count = (Count) session.getAttribute("count");
        if (count == null) {
            count = new Count();
            count.count = 0;
            session.setAttribute("count", count);
        }
        
        // Get or create score for this session
        Integer score = (Integer) session.getAttribute("score");
        if (score == null) {
            score = 0;
            session.setAttribute("score", score);
        }
        
        System.out.println("Session ID: " + session.getId() + " Count is " + count.getCount());
        
        GetQuestion getQuestion = new GetQuestion();
        QuestionTrueFalse qtf = getQuestion.nextQuestion(count.getCount());
        
        MyString myStringObject = new MyString();
        myStringObject.setMyString(qtf.getQuestion());
        
        model.addAttribute("count", count);
        model.addAttribute("myString", myStringObject);
        model.addAttribute("score", score);

        return "question";
    }

    @PostMapping({"/get_question", "/get-question"})
    public String questionFormPOST(@RequestParam String answer, 
                                   Model model,
                                   HttpSession session) {
        // Get count from session
        Count count = (Count) session.getAttribute("count");
        if (count == null) {
            count = new Count();
            session.setAttribute("count", count);
        }
        
        // Get score from session
        Integer score = (Integer) session.getAttribute("score");
        if (score == null) {
            score = 0;
            session.setAttribute("score", score);
        }
        
        System.out.println("Session ID: " + session.getId() + " The answer is " + answer);
        
        GetQuestion getQuestion = new GetQuestion();
        QuestionTrueFalse qtf = getQuestion.nextQuestion(count.count);
        
        // Check answer and update score
        Boolean answerBool = Boolean.valueOf(answer);
        boolean isCorrect = answerBool.equals(qtf.getAnswer());
        
        if (isCorrect) {
            System.out.println("Correct!");
            score++;
        } else {
            System.out.println("Wrong!");
        }
        
        // Update session
        session.setAttribute("score", score);
        
        // Increment count for next question
        count.count = count.count + 1;
        session.setAttribute("count", count);
        
        // Prepare next question
        QuestionTrueFalse nextQtf = getQuestion.nextQuestion(count.count);
        MyString myStringObject = new MyString();
        myStringObject.setMyString(nextQtf.getQuestion());
        
        model.addAttribute("count", count);
        model.addAttribute("myString", myStringObject);
        model.addAttribute("score", score);
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("lastAnswer", qtf.getAnswer());
        
        return "question";
    }
    
    @GetMapping("/reset")
    public String reset(HttpSession session) {
        session.invalidate();
        return "redirect:/get_question";
    }
}