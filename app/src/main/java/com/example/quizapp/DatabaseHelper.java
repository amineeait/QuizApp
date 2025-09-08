package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1; // Reverted back to 1

    // --- User Table ---
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    // --- Question Table ---
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_QUESTION_ID = "id";
    private static final String COLUMN_QUESTION_TEXT = "question_text";
    private static final String COLUMN_OPTION1 = "option1";
    private static final String COLUMN_OPTION2 = "option2";
    private static final String COLUMN_OPTION3 = "option3";
    private static final String COLUMN_OPTION4 = "option4";
    private static final String COLUMN_ANSWER_NR = "answer_nr";
    private static final String COLUMN_DIFFICULTY = "difficulty";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Questions Table
        final String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + " ("
                + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUESTION_TEXT + " TEXT, "
                + COLUMN_OPTION1 + " TEXT, "
                + COLUMN_OPTION2 + " TEXT, "
                + COLUMN_OPTION3 + " TEXT, "
                + COLUMN_OPTION4 + " TEXT, "
                + COLUMN_ANSWER_NR + " INTEGER, "
                + COLUMN_DIFFICULTY + " TEXT"
                + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        insertInitialQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This will delete the tables and recreate them if the version number changes.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // --- User Methods ---
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password); // Note: Hashing should be implemented here for security
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // --- Question Methods ---
    public List<Question> getRandomQuestionsByDifficulty(String difficulty, int limit) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_DIFFICULTY + " = ? ORDER BY RANDOM() LIMIT ?",
                new String[]{difficulty, String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT));
                String option1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION1));
                String option2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION2));
                String option3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION3));
                String option4 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION4));
                int answerNr = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANSWER_NR));
                questionList.add(new Question(questionText, option1, option2, option3, option4, answerNr));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private void insertInitialQuestions(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            insertEasyQuestions(db);
            insertMediumQuestions(db);
            insertHardQuestions(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertQuestion(SQLiteDatabase db, String question, String o1, String o2, String o3, String o4, int answerNr, String difficulty) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEXT, question);
        values.put(COLUMN_OPTION1, o1);
        values.put(COLUMN_OPTION2, o2);
        values.put(COLUMN_OPTION3, o3);
        values.put(COLUMN_OPTION4, o4);
        values.put(COLUMN_ANSWER_NR, answerNr);
        values.put(COLUMN_DIFFICULTY, difficulty);
        db.insert(TABLE_QUESTIONS, null, values);
    }

    // --- All 90 Questions ---

    private void insertEasyQuestions(SQLiteDatabase db) {
        String difficulty = "easy";
        insertQuestion(db, "What is the capital of Japan?", "Beijing", "Seoul", "Tokyo", "Bangkok", 3, difficulty);
        insertQuestion(db, "How many days are in a week?", "5", "6", "7", "8", 3, difficulty);
        insertQuestion(db, "What color is the sky on a clear day?", "Green", "Blue", "Red", "Yellow", 2, difficulty);
        insertQuestion(db, "Which animal is known as 'man's best friend'?", "Cat", "Horse", "Dog", "Goldfish", 3, difficulty);
        insertQuestion(db, "What is 10 + 5?", "12", "15", "18", "20", 2, difficulty);
        insertQuestion(db, "Which planet is closest to the Sun?", "Venus", "Earth", "Mercury", "Mars", 3, difficulty);
        insertQuestion(db, "What do bees make?", "Honey", "Wax", "Silk", "Pollen", 1, difficulty);
        insertQuestion(db, "How many continents are there?", "5", "6", "7", "8", 3, difficulty);
        insertQuestion(db, "What is the opposite of 'hot'?", "Warm", "Cold", "Spicy", "Cool", 2, difficulty);
        insertQuestion(db, "Which of these is a fruit?", "Carrot", "Broccoli", "Apple", "Potato", 3, difficulty);
        insertQuestion(db, "What is the name of the toy cowboy in 'Toy Story'?", "Buzz", "Woody", "Andy", "Rex", 2, difficulty);
        insertQuestion(db, "How many sides does a triangle have?", "2", "3", "4", "5", 2, difficulty);
        insertQuestion(db, "What is the main language spoken in Spain?", "Portuguese", "Italian", "French", "Spanish", 4, difficulty);
        insertQuestion(db, "What is the first letter of the alphabet?", "A", "B", "C", "D", 1, difficulty);
        insertQuestion(db, "Which ocean is the largest?", "Atlantic", "Indian", "Arctic", "Pacific", 4, difficulty);
        insertQuestion(db, "What is a baby cat called?", "A puppy", "A kitten", "A calf", "A foal", 2, difficulty);
        insertQuestion(db, "In which city is the Eiffel Tower located?", "Rome", "London", "Paris", "Berlin", 3, difficulty);
        insertQuestion(db, "Which fairy tale character had a glass slipper?", "Sleeping Beauty", "Snow White", "Cinderella", "Belle", 3, difficulty);
        insertQuestion(db, "What is the primary gas we breathe in?", "Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen", 1, difficulty);
        insertQuestion(db, "What is the shape of a standard stop sign?", "Circle", "Square", "Triangle", "Octagon", 4, difficulty);
        insertQuestion(db, "What is the capital of Italy?", "Madrid", "Lisbon", "Rome", "Athens", 3, difficulty);
        insertQuestion(db, "How many wheels does a bicycle have?", "1", "2", "3", "4", 2, difficulty);
        insertQuestion(db, "Who was the first President of the United States?", "Thomas Jefferson", "Abraham Lincoln", "George Washington", "John Adams", 3, difficulty);
        insertQuestion(db, "What is the color of a banana?", "Red", "Blue", "Green", "Yellow", 4, difficulty);
        insertQuestion(db, "What is the sound a lion makes?", "Moo", "Roar", "Oink", "Chirp", 2, difficulty);
        insertQuestion(db, "Which of these is not a primary color?", "Red", "Yellow", "Green", "Blue", 3, difficulty);
        insertQuestion(db, "Which season comes after summer?", "Winter", "Spring", "Autumn", "Monsoon", 3, difficulty);
        insertQuestion(db, "What is the currency of the United States?", "Euro", "Yen", "Pound", "Dollar", 4, difficulty);
        insertQuestion(db, "What planet is known as the Red Planet?", "Jupiter", "Mars", "Saturn", "Venus", 2, difficulty);
        insertQuestion(db, "How many letters are in the English alphabet?", "24", "25", "26", "27", 3, difficulty);
    }

    private void insertMediumQuestions(SQLiteDatabase db) {
        String difficulty = "medium";
        insertQuestion(db, "Who wrote the play 'Romeo and Juliet'?", "Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain", 2, difficulty);
        insertQuestion(db, "What is the chemical symbol for gold?", "Ag", "Au", "Fe", "Pb", 2, difficulty);
        insertQuestion(db, "In what year did the Titanic sink?", "1905", "1912", "1918", "1923", 2, difficulty);
        insertQuestion(db, "Which country is known as the Land of the Rising Sun?", "China", "South Korea", "Japan", "Thailand", 3, difficulty);
        insertQuestion(db, "What is the hardest natural substance on Earth?", "Gold", "Iron", "Diamond", "Quartz", 3, difficulty);
        insertQuestion(db, "Who painted the Mona Lisa?", "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet", 3, difficulty);
        insertQuestion(db, "Which is the longest river in the world?", "Amazon River", "Nile River", "Yangtze River", "Mississippi River", 2, difficulty);
        insertQuestion(db, "What is the powerhouse of the cell?", "Nucleus", "Ribosome", "Mitochondrion", "Cell Wall", 3, difficulty);
        insertQuestion(db, "What does 'CPU' stand for?", "Central Process Unit", "Computer Personal Unit", "Central Processing Unit", "Computer Processor Unit", 3, difficulty);
        insertQuestion(db, "Which country gifted the Statue of Liberty to the USA?", "Germany", "Spain", "United Kingdom", "France", 4, difficulty);
        insertQuestion(db, "What is the capital city of Australia?", "Sydney", "Melbourne", "Canberra", "Perth", 3, difficulty);
        insertQuestion(db, "How many bones are in the adult human body?", "206", "212", "201", "220", 1, difficulty);
        insertQuestion(db, "Who invented the telephone?", "Thomas Edison", "Nikola Tesla", "Alexander Graham Bell", "Guglielmo Marconi", 3, difficulty);
        insertQuestion(db, "Which element has the atomic number 1?", "Helium", "Oxygen", "Hydrogen", "Lithium", 3, difficulty);
        insertQuestion(db, "In which year did World War II end?", "1942", "1945", "1948", "1950", 2, difficulty);
        insertQuestion(db, "What is the largest mammal in the world?", "Elephant", "Blue Whale", "Giraffe", "Great White Shark", 2, difficulty);
        insertQuestion(db, "Which artist cut off his own ear?", "Pablo Picasso", "Claude Monet", "Salvador Dalí", "Vincent van Gogh", 4, difficulty);
        insertQuestion(db, "What is the main ingredient in guacamole?", "Tomato", "Avocado", "Onion", "Lime", 2, difficulty);
        insertQuestion(db, "What is the square root of 144?", "10", "11", "12", "14", 3, difficulty);
        insertQuestion(db, "What is the name of the galaxy our solar system is in?", "Andromeda", "Triangulum", "Whirlpool", "Milky Way", 4, difficulty);
        insertQuestion(db, "Which country won the first ever FIFA World Cup in 1930?", "Argentina", "Brazil", "Uruguay", "Italy", 3, difficulty);
        insertQuestion(db, "What is 'cynophobia' the fear of?", "Spiders", "Dogs", "Heights", "Confined spaces", 2, difficulty);
        insertQuestion(db, "Who discovered penicillin?", "Marie Curie", "Louis Pasteur", "Alexander Fleming", "Isaac Newton", 3, difficulty);
        insertQuestion(db, "Which planet has the most moons?", "Jupiter", "Saturn", "Uranus", "Neptune", 2, difficulty);
        insertQuestion(db, "What is the capital of Canada?", "Toronto", "Vancouver", "Montreal", "Ottawa", 4, difficulty);
        insertQuestion(db, "What is the largest desert in the world?", "Sahara Desert", "Arabian Desert", "Gobi Desert", "Antarctic Polar Desert", 4, difficulty);
        insertQuestion(db, "In Greek mythology, who is the god of the sea?", "Zeus", "Hades", "Poseidon", "Apollo", 3, difficulty);
        insertQuestion(db, "Which movie won the first-ever Academy Award for Best Picture?", "The Jazz Singer", "Wings", "Metropolis", "Sunrise", 2, difficulty);
        insertQuestion(db, "What is the world's most spoken language by number of native speakers?", "English", "Mandarin Chinese", "Spanish", "Hindi", 2, difficulty);
        insertQuestion(db, "What is the boiling point of water at sea level in Celsius?", "90°C", "100°C", "110°C", "120°C", 2, difficulty);
    }

    private void insertHardQuestions(SQLiteDatabase db) {
        String difficulty = "hard";
        insertQuestion(db, "What is the name of the deepest point in the Earth's oceans?", "Tonga Trench", "Philippine Trench", "Mariana Trench", "Kermadec Trench", 3, difficulty);
        insertQuestion(db, "In what year was the first personal computer, the Kenbak-1, introduced?", "1971", "1975", "1981", "1968", 1, difficulty);
        insertQuestion(db, "Which philosopher wrote 'Critique of Pure Reason'?", "Nietzsche", "Hegel", "Kant", "Spinoza", 3, difficulty);
        insertQuestion(db, "What is the common name for the chemical compound H2O2?", "Distilled Water", "Hydrogen Peroxide", "Heavy Water", "Sodium Chloride", 2, difficulty);
        insertQuestion(db, "The 'Hundred Years' War' was primarily fought between which two countries?", "England and Spain", "France and Spain", "England and France", "Germany and Russia", 3, difficulty);
        insertQuestion(db, "What is the least common blood type in humans?", "O+", "A-", "B+", "AB-", 4, difficulty);
        insertQuestion(db, "Who is credited with the discovery of the electron?", "Ernest Rutherford", "J.J. Thomson", "Niels Bohr", "James Chadwick", 2, difficulty);
        insertQuestion(db, "What does the 'C' in E=mc² stand for?", "Charge", "Constant", "Speed of light", "Mass of carbon", 3, difficulty);
        insertQuestion(db, "Which Shakespeare play features the character Iago?", "Hamlet", "Macbeth", "Othello", "King Lear", 3, difficulty);
        insertQuestion(db, "In computer science, what does 'NP' in 'NP-hard' problems stand for?", "Non-Polynomial", "Non-deterministic Polynomial", "Normally Processed", "Notationally Precise", 2, difficulty);
        insertQuestion(db, "What is the capital of Bhutan?", "Kathmandu", "Thimphu", "Dhaka", "Naypyidaw", 2, difficulty);
        insertQuestion(db, "The Battle of Agincourt, a major English victory in the Hundred Years' War, occurred in what year?", "1388", "1415", "1453", "1485", 2, difficulty);
        insertQuestion(db, "What is the most abundant element in the Earth's crust?", "Iron", "Silicon", "Aluminum", "Oxygen", 4, difficulty);
        insertQuestion(db, "Which composer wrote the famous classical piece 'The Four Seasons'?", "Bach", "Mozart", "Beethoven", "Vivaldi", 4, difficulty);
        insertQuestion(db, "What is the study of fungi called?", "Mycology", "Phycology", "Virology", "Bryology", 1, difficulty);
        insertQuestion(db, "Which physicist is known for the uncertainty principle?", "Max Planck", "Werner Heisenberg", "Erwin Schrödinger", "Paul Dirac", 2, difficulty);
        insertQuestion(db, "What is the value of the mathematical constant 'e' (Euler's number) to two decimal places?", "3.14", "1.62", "2.72", "9.81", 3, difficulty);
        insertQuestion(db, "Who was the Roman god of war?", "Jupiter", "Mars", "Neptune", "Mercury", 2, difficulty);
        insertQuestion(db, "What is the only country in the world that is a 'double landlocked' country?", "Liechtenstein", "Uzbekistan", "Andorra", "Luxembourg", 2, difficulty);
        insertQuestion(db, "Which chemical element is named after the creator of the periodic table?", "Curium", "Einsteinium", "Fermium", "Mendelevium", 4, difficulty);
        insertQuestion(db, "What is the term for a word that is spelled the same forwards and backwards?", "Palindrome", "Anagram", "Onomatopoeia", "Antonym", 1, difficulty);
        insertQuestion(db, "In 'The Lord of the Rings', what is the name of the wizard who becomes 'the White'?", "Saruman", "Radagast", "Gandalf", "Alatar", 3, difficulty);
        insertQuestion(db, "What is the primary export of the fictional country of Wakanda?", "Gold", "Diamonds", "Vibranium", "Oil", 3, difficulty);
        insertQuestion(db, "Which novel begins with the line 'It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.'?", "Moby Dick", "War and Peace", "Pride and Prejudice", "Jane Eyre", 3, difficulty);
        insertQuestion(db, "What is the Schwarzschild radius?", "Radius of a black hole's event horizon", "Radius of an electron's orbit", "The mean radius of the sun", "The radius of the observable universe", 1, difficulty);
        insertQuestion(db, "What language was the New Testament originally written in?", "Hebrew", "Latin", "Aramaic", "Koine Greek", 4, difficulty);
        insertQuestion(db, "The ancient city of Carthage is located in which modern-day country?", "Egypt", "Greece", "Tunisia", "Italy", 3, difficulty);
        insertQuestion(db, "Which of these is not one of the noble gases?", "Argon", "Xenon", "Radon", "Nitrogen", 4, difficulty);
        insertQuestion(db, "What is the derivative of x²?", "2x", "x³/3", "x", "2", 1, difficulty);
        insertQuestion(db, "Who was the first female Prime Minister of the United Kingdom?", "Theresa May", "Margaret Thatcher", "Angela Merkel", "Indira Gandhi", 2, difficulty);
    }
}