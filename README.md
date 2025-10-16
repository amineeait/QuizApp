🎓 QuizApp — Interactive Offline Android Quiz Application
🧩 Overview

QuizApp is a fully offline Android application designed to provide an engaging and interactive quiz experience. Users can register, log in, and take quizzes with randomized questions, all without requiring an internet connection. The app supports multiple difficulty levels and gives immediate feedback for a modern, educational, and fun experience.

🌟 Features
✅ Full Offline Functionality

Works completely offline using a local SQLite database.

Stores both user accounts and quiz questions locally.

🔐 User Authentication Flow

Secure registration and login system.

Prevents duplicate accounts with unique email constraints.

Personalized experience for each user.

🎲 Dynamic Quiz Engine

Randomly fetches 10 questions per quiz.

Supports difficulty levels: Easy, Medium, Hard.

Includes CountDownTimer for each question.

Immediate visual feedback for correct/incorrect answers.

🖌️ Modern UI/UX

Built with Material Design for a clean, intuitive interface.

Uses TextInputLayout for forms and CardView for question grouping.

Circular ProgressBar shows a quick score summary on the results screen.

📊 Results and Sharing

Displays detailed results with score breakdown.

Option to share quiz results for social engagement.

🛠️ Technical Implementation
Core Technology

Platform: Native Android

IDE: Android Studio

Language: Java

Data Persistence

Managed with DatabaseHelper.java, extending SQLiteOpenHelper.

Tables:

Table	Purpose	Schema (Columns)	Key Constraint
users	User authentication	id (PRIMARY KEY), email, password	email UNIQUE
questions	Quiz content repository	id (PRIMARY KEY), question_text, option1, option2, option3, option4, answer_nr, difficulty	N/A
Key Database Queries

checkUser() — Authenticates user login.

getRandomQuestionsByDifficulty() — Fetches 10 random questions based on selected difficulty.

📱 Application Flow
Primary Flow
Login Screen → Level Select Screen → Quiz Screen → Results Screen

Registration Flow
Login Screen → Create Account → Register Screen → Level Select Screen

🧩 Model Classes
Question.java

Represents a single quiz question.

Fields: question, option1, option2, option3, option4 (Strings), answerNr (int).

Encapsulates question data for Activities to access using getters.

🚀 Future Enhancements

Security: Password hashing for stronger protection.

User Engagement: High score tracking and leaderboards.

Refactoring: Introduce User.java for better user data handling.

UI Improvements: Animations and enhanced feedback for better interactivity.

📂 Project Structure
app/
├── java/com/quizapp/
│   ├── DatabaseHelper.java
│   ├── Question.java
│   ├── Activities/
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── QuizActivity.java
│   │   └── ResultActivity.java
├── res/
│   ├── layout/
│   ├── drawable/
│   └── values/
└── AndroidManifest.xml

📫 Contact & Support

For questions, suggestions, or bug reports, feel free to reach out:

Author: Mohamed Amine Ait El Mahjoub
Email: your.email@example.com

GitHub: github.com/yourusername

🤝 Contributing

Contributions are welcome! You can help by:

Reporting issues

Suggesting features

Submitting pull requests

Please follow the coding style and comment your code clearly.

⚖️ License

This project is licensed under the MIT License — see the LICENSE
 file for details.
