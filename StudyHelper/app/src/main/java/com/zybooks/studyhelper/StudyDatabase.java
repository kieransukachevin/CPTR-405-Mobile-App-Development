package com.zybooks.studyhelper;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class, Subject.class}, version = 1)
public abstract class StudyDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "study.db";

    private static StudyDatabase mStudyDatabase;

    // Singleton
    public static StudyDatabase getInstance(Context context) {
        if (mStudyDatabase == null) {
            mStudyDatabase = Room.databaseBuilder(context, StudyDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
            mStudyDatabase.addStarterData();
        }

        return mStudyDatabase;
    }

    public abstract QuestionDao questionDao();
    public abstract SubjectDao subjectDao();

    private void addStarterData() {

        // Add a few subjects and questions if database is empty
        if (subjectDao().getSubjects().size() == 0) {

            // Execute code on a background thread
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Subject subject = new Subject("Math");
                    long subjectId = subjectDao().insertSubject(subject);

                    Question question = new Question();
                    question.setText("What is 2 + 3?");
                    question.setAnswer("2 + 3 = 5");
                    question.setSubjectId(subjectId);
                    questionDao().insertQuestion(question);

                    question = new Question();
                    question.setText("What is pi?");
                    question.setAnswer("Pi is the ratio of a circle's circumference to its diameter.");
                    question.setSubjectId(subjectId);
                    questionDao().insertQuestion(question);

                    subject = new Subject("History");
                    subjectId = subjectDao().insertSubject(subject);

                    question = new Question();
                    question.setText("On what date was the U.S. Declaration of Independence adopted?");
                    question.setAnswer("July 4, 1776.");
                    question.setSubjectId(subjectId);
                    questionDao().insertQuestion(question);

                    subject = new Subject("Computing");
                    subjectId = subjectDao().insertSubject(subject);
                }
            });
        }
    }
}