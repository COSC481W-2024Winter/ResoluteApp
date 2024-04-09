package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Reply_IT {

    /*TESTS NEEDED:
        - Until #44 is merged allowing logged exercises to populate their friends' inboxes, there is
            no way to write a flowing log->inbox->view reply test. Even then, the test would not be
            continuous until there is functionality to delete exercises from the PrevActivity page.
     */

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

    //This function navigates to the PrevActivityFragment before every test
    @Before
    public void navigateToPreviousActivityPage() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());

        Thread.sleep(1000);
    }

    //Test to check if an exercise with no replies has the appropriate notifier
    @Test
    public void checkEmptyReplies() throws InterruptedException {
        //Click on exercise with known lack of replies
        onView(withText("REPLYNAVIGATIONTEST")).perform(click());
        Thread.sleep(1000);

        //Check for existence of "No replies yet..." text, meaning replies collection is empty
        onView(withText("No replies yet...")).perform(click());
    }

    //Test to check if navigating between multiple exercises utilizes the correct document ids
    @Test
    public void checkMultipleReplies() throws InterruptedException {
        //Click on test exercise 1
        onView(withText("REPLYTESTEXERCISE 1")).perform(click());
        Thread.sleep(1000);
        //Check for reply1
        onView(withText("friend1 says \"testreply1\"")).perform(click());

        //Exit ReplyFragment
        onView(withId(R.id.to_prev_from_reply)).perform(click());
        Thread.sleep(1000);

        //Click on test exercise 2
        onView(withText("REPLYTESTEXERCISE 2")).perform(click());
        Thread.sleep(1000);
        //Check for reply2
        onView(withText("friend2 says \"testreply2\"")).perform(click());
        //Ensure that reply1 no longer exists in this fragment
        onView(withText("friend1 says \"testreply1\"")).check(doesNotExist());
    }

    //Test to check if an exercise gets deleted
    @Test
    public void checkDeletion() throws InterruptedException {
        onView(withId(R.id.to_home_from_previous_activity)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_profile_from_home)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.logout_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.login_username_entry)).perform(typeText("UserR"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("PasswordR"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.editTextExercise)).perform(typeText("Reply delete exercise test"), closeSoftKeyboard());
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.editTextUnits)).perform(typeText("Test"), closeSoftKeyboard());

        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_home_from_log_exercise)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());
        Thread.sleep(1000);

        onView(withText("Reply delete exercise test")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.delete_exercise)).perform(click());
        Thread.sleep(1000);

        onView(withText("Reply delete exercise test")).check(doesNotExist());

        onView(withId(R.id.to_home_from_previous_activity)).perform(click());
        Thread.sleep(1000);

        //Clears exercise from inbox
        onView(withId(R.id.to_inbox_from_home)).perform(click());
        Thread.sleep(1000);

        onView(withText("Reply delete exercise test")).perform(click());
        Thread.sleep(1000);

        onView(withText("Send")).perform(click());
    }
}