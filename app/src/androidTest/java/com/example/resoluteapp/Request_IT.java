package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Request_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToLogExerciseScreen() throws InterruptedException {
        // Navigate to the log exercise screen before each test
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // You may need to add a delay here if navigation is asynchronous
        Thread.sleep(1000);
        onView(withId(R.id.to_friends_from_home)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.to_request_from_friends)).perform(click());
        Thread.sleep(1000);

    }

    @Test
    public void navigateBack() throws InterruptedException {

        onView(withId(R.id.to_friends_from_request)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.friends_identifier)).check(matches(isDisplayed()));

    }


}