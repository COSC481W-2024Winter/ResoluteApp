package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.not;

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
public class Friends_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    //This function clears the SharedPreferences file before and after every test
    @Before
    @After
    public void emptySharedPref(){
        SharedPreferences sp = getInstrumentation().getTargetContext().getSharedPreferences("com.example.ResoluteApp.SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void enterUsernameClears() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_friends_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.enter_username)).perform(typeText("testUser2"), closeSoftKeyboard());
        onView(withId(R.id.send_request)).perform(click());

        onView(withId(R.id.enter_username)).check(matches(withText("")));
    }

    @Test
    public void generateFriendsTable() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_friends_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.friends_table)).check(matches(isDisplayed()));
    }

    //Relies on testUser2 existing in friends_testUser1 collection
    @Test
    public void removeOption() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_friends_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withText("testUser2")).perform(click());

        Thread.sleep(1000);

        onView(withText("Remove")).check(matches(withText("Remove")));
    }

    //Relies on testUser2 existing in friends_testUser1 collection
    //Relies on testUser1 existing in friends_testUser2 collection
    @Test
    public void removeFriend() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("testUser1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_friends_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withText("testUser2")).perform(click());

        Thread.sleep(1000);

        onView(withText("Remove")).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_profile_from_friends)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.logout_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.login_username_entry)).perform(typeText("testUser2"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("testUser2"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_friends_from_home)).perform(click());

        Thread.sleep(1000);

        onView(withText("testUser1")).check(doesNotExist());
    }
}