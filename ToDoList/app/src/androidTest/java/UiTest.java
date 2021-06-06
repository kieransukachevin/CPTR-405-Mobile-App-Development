package com.zybooks.to_dolist;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class UiTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testAddingAnItem() {

        // Click Clear button
        onView(withText("Clear")).perform(click());

        // Enter "Feed dog" into the EditText
        onView(withId(R.id.toDoItem)).perform(typeText("Feed dog"));

        // Click Add button
        onView(withId(R.id.addButton)).perform(click());

        // Verify item added to to-do list
        onView(withId(R.id.itemList)).check(matches(withText("1. Feed dog\n")));
    }
}