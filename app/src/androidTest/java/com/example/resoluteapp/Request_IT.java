package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import android.content.Context;
import android.content.SharedPreferences;
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
public class Request_IT {

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
    public void navigateToLogExerciseScreen() throws InterruptedException {
        // Navigate to the log exercise screen before each test
        onView(withId(R.id.login_username_entry)).perform(typeText("User3"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password3"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

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

    }

    //This test assumes there is a request from User20 in requests_User3. Test is not repeatable without database alteration.
    @Test
    public void testRowDeletionOnDeny() {

        onView(allOf(withText(R.string.deny), isDescendantOfA(allOf(instanceOf(TableRow.class), isDisplayed())),
                hasSibling(withText("User20")), // Replace with First Row Username
                isDisplayed()
        )).perform(click());

        onView(withText("User20")).check(doesNotExist());
    }


    //This test assumes there is a request from User30 in requests_User3. Test is not repeatable without database alteration.
    @Test
    public void testRowDeletionOnApprove() {

        onView(allOf(withText(R.string.approve), isDescendantOfA(allOf(instanceOf(TableRow.class), isDisplayed())),
                hasSibling(withText("User30")), // Replace with First Row Username
                isDisplayed()
        )).perform(click());

        onView(withText("User30")).check(doesNotExist());

    }

}
