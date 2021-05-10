package com.zybooks.studyhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import java.util.List;

public class ImportActivity extends AppCompatActivity {

    private LinearLayout mSubjectLayoutContainer;
    private StudyFetcher mStudyFetcher;
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        mSubjectLayoutContainer = findViewById(R.id.subjectLayout);

        // Show progress bar
        mLoadingProgressBar = findViewById(R.id.loadingProgressBar);
        mLoadingProgressBar.setVisibility(View.VISIBLE);

        mStudyFetcher = new StudyFetcher(this);
        mStudyFetcher.fetchSubjects(mFetchListener);
    }

    private StudyFetcher.OnStudyDataReceivedListener mFetchListener = new StudyFetcher.OnStudyDataReceivedListener() {

        @Override
        public void onSubjectsReceived(List<Subject> subjects) {

            // Hide progress bar
            mLoadingProgressBar.setVisibility(View.GONE);

            // Create a checkbox for each subject
            for (Subject subject: subjects) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setTextSize(24);
                checkBox.setText(subject.getText());
                checkBox.setTag(subject);
                mSubjectLayoutContainer.addView(checkBox);
            }
        }

        @Override
        public void onQuestionsReceived(Subject subject, List<Question> questions) {

            if (questions.size() > 0) {
                StudyDatabase studyDb = StudyDatabase.getInstance(getApplicationContext());

                // Add the questions to the database
                for (Question question : questions) {
                    question.setSubjectId(subject.getId());
                    studyDb.questionDao().insertQuestion(question);
                }

                Toast.makeText(getApplicationContext(), subject.getText() + " imported successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), subject.getText() + " contained no questions",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Error loading subjects. Try again later.",
                    Toast.LENGTH_LONG).show();
            error.printStackTrace();
            mLoadingProgressBar.setVisibility(View.GONE);
        }
    };

    public void importButtonClick(View view) {

        StudyDatabase studyDb = StudyDatabase.getInstance(getApplicationContext());

        // Determine which subjects were selected
        int numCheckBoxes = mSubjectLayoutContainer.getChildCount();
        for (int i = 0; i < numCheckBoxes; i++) {
            CheckBox checkBox = (CheckBox) mSubjectLayoutContainer.getChildAt(i);
            if (checkBox.isChecked()) {
                Subject subject = (Subject) checkBox.getTag();

                // See if this subject has already been imported
                if (studyDb.subjectDao().getSubjectByText(subject.getText()) == null) {
                    // Add subject to the database and import
                    long newId = studyDb.subjectDao().insertSubject(subject);
                    subject.setId(newId);
                    mStudyFetcher.fetchQuestions(subject, mFetchListener);
                }
                else {
                    Toast.makeText(this, subject.getText() + " is already imported.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}