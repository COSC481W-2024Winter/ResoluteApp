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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogIn_IT {

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
    public void checkLoginSuccess() throws InterruptedException {

        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.welcome_message)).check(matches(withText("Hello " + "User1" + "\nWelcome to Resolute!")));
    }

    @Test
    public void aDifferentLoginTest() throws InterruptedException{
    }
  
    @Test
    public void checkIncorrectUsernamePassword() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("User2"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(2000);


        //Check that element from home fragment is not accessible, ensuring login failed and Login Fragment is still active
        onView(withId(R.id.to_friends_from_home)).check(doesNotExist());
    }

    @Test
    public void checkEmptyField() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("User2"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from home fragment is not accessible, ensuring login failed and Login Fragment is still active
        onView(withId(R.id.to_friends_from_home)).check(doesNotExist());

        //Clear fields
        onView(withId(R.id.login_username_entry)).perform(clearText());

        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from home fragment is not accessible, ensuring login failed and Login Fragment is still active
        onView(withId(R.id.to_friends_from_home)).check(doesNotExist());

        //Clear fields
        onView(withId(R.id.login_password_entry)).perform(clearText());

        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from home fragment is not accessible, ensuring login failed and Login Fragment is still active
        onView(withId(R.id.to_friends_from_home)).check(doesNotExist());
    }
}
