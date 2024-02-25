package com.example.resoluteapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.login_username_entry))
                .perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry))
                .perform(typeText("Password1"), closeSoftKeyboard());

        // Type text and then press the button.
        onView(withId(R.id.login_button))
                .perform(click());

        // Check that the text was changed.
        // onView(withId(R.id.welcome_message)).check(matches(withText("Hello User1 \nWelcome to Resolute!"))); // will succeed
        onView(withId(R.id.login_identifier)).check(matches(withText("Resolute1"))); // will fail
    }

}