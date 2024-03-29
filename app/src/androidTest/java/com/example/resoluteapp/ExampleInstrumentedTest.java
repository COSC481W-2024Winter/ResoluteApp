package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    // this tells the testing file which activity we're in
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    // This is one test method. Should test one Thing related to this class.
    public void changeText_sameActivity() throws InterruptedException {

        // onView basically grabs the view that IS UP when you want the method to execute.
        // withId is telling the application which component to grab
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        // Type text and then press the button.
        onView(withId(R.id.login_button)).perform(click());

        // not a recommended way of slowing the system down but it works for now
        // the system does need time to get caught up
        Thread.sleep(2000);

        // This just checks "is welcome_message up on the screen?"
        onView(withId(R.id.welcome_message)).check(matches(isDisplayed()));

        // This checks that the welcome message displays information related to our actual test
        onView(withId(R.id.welcome_message)).check(matches(withText("Hello " + "User1" + "\nWelcome to Resolute!"))); // will succeed
        // onView(withId(R.id.login_identifier)).check(matches(withText("Resolute1"))); // will fail
    }

}