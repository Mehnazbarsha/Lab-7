Lab 7: Thymeleaf Quiz Application
Description
This Spring Boot application implements a True/False quiz that supports multiple independent quiz takers simultaneously. Each user has their own session with separate question tracking and score counting.
Key Features:

Independent sessions for multiple users (different browser windows/tabs maintain separate state)
Score tracking per session
Immediate feedback on answers
Reset functionality

What Was Done

Fixed Session Independence: Changed from @SessionAttributes to HttpSession in HomeController.java so each user has their own separate session
Added Score Tracking: Each session tracks correct answers independently
Enhanced UI: Added feedback messages showing if answers are correct/incorrect
Comprehensive Testing:

17 unit tests for model classes (ModelTests.java)
10 integration tests including session independence verification (IntegrationTests.java)



How to Run
bash# Build the project
./mvnw clean install

# Run tests (all 28 tests should pass)
./mvnw test

# Start the application
./mvnw spring-boot:run
Access the Application
Open browser to: http://localhost:8080/get_question
To test multiple independent users: Open multiple browser windows - each will maintain its own question count and score.
Project Structure

src/main/java/com/example/demo/controller/HomeController.java - Main controller with session management
src/main/java/com/example/demo/model/ - Model classes (Count, GetQuestion, QuestionTrueFalse, etc.)
src/main/resources/templates/question.html - Quiz interface
src/test/java/com/example/demo/ModelTests.java - Unit tests
src/test/java/com/example/demo/IntegrationTests.java - Functional tests