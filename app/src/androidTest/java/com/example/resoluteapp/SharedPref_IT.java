package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.Suppress;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/***********INFO************
    This test file exists for the sake of testing features related to the use of the
    SharedPreferences file. This file was added to add tests related to opening and closing the app
    while logged in, or logged out.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SharedPref_IT {

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

    //Test used to check that closing the app and relaunching while logged in skips login screen
    @Test
    public void checkStayLogin(){
        //Login at the start
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        //Close the activity and relaunch
        activityScenarioRule.getScenario().close();
        ActivityScenario.launch(MainActivity.class, null);

        //Check if HomeFragment is being displayed by clicking "Log exercise"
        onView(withId(R.id.to_log_exercise_button)).perform(click());
    }

    //Test used to check the functionality of Logout button
    @Test
    public void checkLogout() throws InterruptedException{
        //Login at the start
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(1000);

        //Navigate to profile and click logout
        onView(withId(R.id.to_profile_from_home)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());

        //Close the activity and relaunch
        activityScenarioRule.getScenario().close();
        ActivityScenario.launch(MainActivity.class, null);

        //Check for the existence of Login Screen
        onView(withId(R.id.login_identifier)).perform(click());
    }


}
