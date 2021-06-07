package com.zybooks.studyhelper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SubjectIntegrationTest {

    private Context mAppContext;
    private Subject mTestSubject;

    @Before
    public void createTestSubject() {
        mAppContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        StudyDatabase studyDb = StudyDatabase.getInstance(mAppContext);

        mTestSubject = new Subject("TEST SUBJECT");
        long newId = studyDb.subjectDao().insertSubject(mTestSubject);
        mTestSubject.setId(newId);

        // Set preferences so newly created subject appears first
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mAppContext);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("subject_order", "new_first");
        editor.apply();
    }

    @After
    public void deleteTestSubject() {
        StudyDatabase studyDb = StudyDatabase.getInstance(mAppContext);
        studyDb.subjectDao().deleteSubject(mTestSubject);
    }

    @Test
    public void testClickSubject() {

        // Initialize Espresso-Intents
        Intents.init();

        // Start SubjectActivity
        ActivityScenario<SubjectActivity> activityScenario =
                ActivityScenario.launch(SubjectActivity.class);

        // Click on first subject in RecyclerView
        onView(withId(R.id.subjectRecyclerView)).perform(actionOnItemAtPosition(0, click()));

        // Verify QuestionActivity started with test subject
        intended(allOf(
                hasComponent(QuestionActivity.class.getName()),
                hasExtra(QuestionActivity.EXTRA_SUBJECT_ID, mTestSubject.getId())
        ));

        // Clean up
        activityScenario.close();

        // Must be called at end of each test case
        Intents.release();
    }
}