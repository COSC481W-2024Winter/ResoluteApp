package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
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
public class ChangePassword_IT {

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

        onView(withId(R.id.to_change_password_from_profile)).perform(click());

        Thread.sleep(1000);

        //Fill 2 fields and leave one blank
        onView(withId(R.id.enter_current_password)).perform(typeText("testUser1"));
        onView(withId(R.id.enter_new_password)).perform(typeText("testUser1"));

        //Clicking "Apply Changes" shouldn't clear the fields
        onView(withId(R.id.apply_changes_button)).perform(click());
        Thread.sleep(1000);

        //Check if fields are still filled
        onView(withId(R.id.enter_current_password)).check(matches(withText("testUser1")));
        onView(withId(R.id.enter_new_password)).check(matches(withText("testUser1")));
    }

    @Test
    public void newPasswordsDoNotMatch() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_profile_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_change_password_from_profile)).perform(click());

        Thread.sleep(1000);

        //Fill new passwords differently
        onView(withId(R.id.enter_current_password)).perform(typeText("testUser1"));
        onView(withId(R.id.enter_new_password)).perform(typeText("testUser1"));
        onView(withId(R.id.confirm_new_password)).perform(typeText("wrong"));

        //Clicking "Apply Changes" shouldn't clear the fields
        onView(withId(R.id.apply_changes_button)).perform(click());

        Thread.sleep(1000);

        //Check that element from home fragment is not accessible
        onView(withId(R.id.welcome_message)).check(doesNotExist());
    }
}