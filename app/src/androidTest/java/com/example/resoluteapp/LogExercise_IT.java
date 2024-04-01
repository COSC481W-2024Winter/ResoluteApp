package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogExercise_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToLogExerciseScreen() throws InterruptedException {
        // Navigate to the log exercise screen before each test
        onView(withId(R.id.login_username_entry)).perform(typeText("Taylor.Jon"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // You may need to add a delay here if navigation is asynchronous
        Thread.sleep(1000);
        onView(withId(R.id.to_log_exercise_button)).perform(click());
        Thread.sleep(1000);
    }

    //This function clears the SharedPreferences file after every test
    @After
    public void emptySharedPref() {
        SharedPreferences sp = getInstrumentation().getTargetContext().getSharedPreferences("com.example.ResoluteApp.SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void blankFieldsTest() throws InterruptedException {
        //Fill only "Exercise" and "Units" fields
        onView(withId(R.id.editTextExercise)).perform(typeText("AutoTestExercise"));
        onView(withId(R.id.editTextUnits)).perform(typeText("AutoTestUnits"));

        //Clicking "Log exercise" shouldn't clear these fields, since there isn't an amount
        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(1000);

        //Check if "Exercise" and "Units" fields are still filled, meaning log failed due to empty field
        onView(withId(R.id.editTextExercise)).check(matches(withText("AutoTestExercise")));
        onView(withId(R.id.editTextUnits)).check(matches(withText("AutoTestUnits")));
    }

    @Test
    public void clearTextFieldsAfterSuccessfulLog() throws InterruptedException {

        onView(withId(R.id.editTextExercise)).perform(typeText("AutoTestExercise"));
        onView(withId(R.id.editTextUnits)).perform(typeText("AutoTestUnits"));
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("AutoTestAmount"), closeSoftKeyboard());

        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.editTextExercise)).check(matches(withText("")));
        onView(withId(R.id.editTextUnits)).check(matches(withText("")));
        onView(withId(R.id.editTextNumberOfUnits)).check(matches(withText("")));

    }

    @Test
    public void sendExerciseToFriend() throws InterruptedException {

        // Log an exercise
        onView(withId(R.id.editTextExercise)).perform(typeText("UNIQUE"));
        onView(withId(R.id.editTextUnits)).perform(typeText("Units"));
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("Amounts"), closeSoftKeyboard());
        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(2000);

        // Log Out and Relaunch
        emptySharedPref();
        activityScenarioRule.getScenario().close();
        ActivityScenario.launch(MainActivity.class, null);

        // Log In As Friend
        onView(withId(R.id.login_username_entry)).perform(typeText("Friend"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Navigate to Inbox
        Thread.sleep(2000);
        onView(withId(R.id.to_inbox_from_home)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.tableLayout)).check(matches(isDisplayed()));
        onView(allOf(withText("UNIQUE"))).perform(click());



    }


}
