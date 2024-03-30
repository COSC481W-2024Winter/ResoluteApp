package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Profile_IT {

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

    @Test
    public void blankFields() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_profile_from_home)).perform(click());

        Thread.sleep(1000);

        //Fill name field and remove text from email field
        onView(withId(R.id.name)).perform(clearText());
        onView(withId(R.id.name)).perform(typeText("name"));
        onView(withId(R.id.email)).perform(clearText());

        //Clicking "Apply Changes" shouldn't clear the fields
        onView(withId(R.id.apply_changes_button)).perform(click());
        Thread.sleep(1000);

        //Check if name field is still filled
        onView(withId(R.id.name)).check(matches(withText("name")));
    }

    @Test
    public void emailAtSymbol() throws InterruptedException{
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_profile_from_home)).perform(click());

        Thread.sleep(1000);

        //Fill name field and email field without an "@"
        onView(withId(R.id.name)).perform(clearText());
        onView(withId(R.id.name)).perform(typeText("name"));
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email)).perform(typeText("email"));

        onView(withId(R.id.apply_changes_button)).perform(click());

        Thread.sleep(1000);

        //Check that element from home fragment is not accessible
        onView(withId(R.id.welcome_message)).check(doesNotExist());
    }
}