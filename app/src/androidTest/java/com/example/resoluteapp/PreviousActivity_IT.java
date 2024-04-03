package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.SharedPreferences;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;

import android.widget.TableRow;

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
public class PreviousActivity_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    //This function clears the SharedPreferences file after every test
    @After
    public void emptySharedPref(){
        SharedPreferences sp = getInstrumentation().getTargetContext().getSharedPreferences("com.example.ResoluteApp.SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    @Before
    public void navigateToPreviousActivityPage() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("Test_User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());

        Thread.sleep(1000);
    }

    @Test
    public void navigationBackHomeTest() throws InterruptedException {
        onView(withId(R.id.to_home_from_previous_activity)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_log_exercise_button)).check(matches(isDisplayed()));
    }

    @Test
    public void orderedTableTest() throws InterruptedException {
        onView(withId(R.id.tableLayout)).check(matches(isDisplayed()));

        onView(allOf(withText(R.id.replies),
                isDescendantOfA(allOf(instanceOf(TableRow.class), isDisplayed()))));

    }

    //Test requires there to NOT exist an exercise in exercises_Test_User1 with exercise String "dummy_test_exercise".
    //Test is not repeatable without database alteration.
    @Test
    public void currentUserTest() throws InterruptedException{
        onView(withId(R.id.to_home_from_previous_activity)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.editTextExercise)).perform(typeText("dummy_test_exercise"));
        onView(withId(R.id.editTextUnits)).perform(typeText("AutoTestUnits"));
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("AutoTestAmount"), closeSoftKeyboard());

        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_home_from_log_exercise)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.tableLayout)).check(matches(isDisplayed()));

        onView(allOf(withText("dummy_test_exercise"))).perform(click());

        onView(allOf(withText("stupid_exercise"))).check(doesNotExist());

    }

}

