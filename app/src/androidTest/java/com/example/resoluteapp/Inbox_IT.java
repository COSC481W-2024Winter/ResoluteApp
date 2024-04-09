package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static java.util.regex.Pattern.matches;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Inbox_IT {

    /*TESTS NEEDED:
        - Choosing to send a reply reloads the page, with the selected document deleted.
          This can be done as part of issue #44
          The selected exercise must be sent and received by User1, meaning User1 and User1 must be
            friends.
          After clicking "Send", check that the document was deleted, then log the exercise again
            for future testing.

        - Choosing to send a reply shows the appropriate reply in PreviousActivity.
          This can be done as part of issue #45
            [ADDENDUM] - The functionality to delete a logged exercise from PrevActivity is no longer
                            a part of issue #45, so this test cannot be made continuous until then.
          The selected exercise must be sent and received by UserR, meaning UserR and UserR must be
            friends.
          After clicking "Send", navigate to previous activity screen, and open the replies for
            the exercise used for testing.
          Check that the correct reply text was received from User1.
          Delete the test exercise.
          Navigate to Log Exercise screen and log the test exercise once again for future testing.
     */

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    //Function that logs in as User1 and navigates to the Inbox screen
    @Before
    public void navigateToInboxScreen() throws InterruptedException {
        // Navigate to the inbox screen before each test
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // You may need to add a delay here if navigation is asynchronous
        Thread.sleep(1000);
        onView(withId(R.id.to_inbox_from_home)).perform(click());
        Thread.sleep(1000);
    }

    //This function clears the SharedPreferences file after every test
    @After
    public void emptySharedPref(){
        SharedPreferences sp = getInstrumentation().getTargetContext().getSharedPreferences("com.example.ResoluteApp.SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    //Test that checks that table populates with documents from "inbox_User1" collection.
    @Test
    public void checkTablePopulation(){
        onView(withText("TEST ITEM")).perform(click()); //Checks if document is filled in the table
        onView(withText("Proud of you!")).perform(click()); //Checks if dialog is displayed on screen
        onView(withText("I'm falling behind!")).perform(click());
        onView(withText("Good hustle!")).perform(click());
        onView(withText("Keep at it!")).perform(click());
    }

    //Test that ensures that choosing to not send a reply does not delete the document
    @Test
    public void noReplyDeletion() throws InterruptedException{
        onView(withText("TEST ITEM")).perform(click()); //Click testing message
        onView(withText("Proud of you!")).perform(click()); //Select a reply
        onView(withText("No, thanks")).perform(click());//Click "No, thanks" to exit dialog
        onView(withId(R.id.to_home_from_inbox)).perform(click());//Click "Home" to exit inbox
        Thread.sleep(1000);
        onView(withId(R.id.to_inbox_from_home)).perform(click());//Click "Inbox" to reload inbox screen with populated table
        Thread.sleep(1000);
        onView(withText("TEST ITEM")).perform(click());//Check that testing message still exists
    }

    //Test that checks if a sent reply is stored in the correct sub-collection
        //Linear. Done by having a user be friends with themself, and reply to their own exercise
    @Test
    public void replySendReceive() throws InterruptedException{
        //Logout as User1
        onView(withId(R.id.to_profile_from_inbox)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.logout_button)).perform(click());
        Thread.sleep(1000);
        //Login as UserR
        onView(withId(R.id.login_username_entry)).perform(typeText("UserR"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("PasswordR"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(1000);
        //Log an exercise
        onView(withId(R.id.to_log_exercise_button)).perform(click());
        onView(withId(R.id.editTextExercise)).perform(typeText("Reply Test"));
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("Reply Amount"));
        onView(withId(R.id.editTextUnits)).perform(typeText("Reply Units"), closeSoftKeyboard());
        onView(withId(R.id.log_exercise_button)).perform(click());
        //Navigate to Inbox
        onView(withId(R.id.to_home_from_log_exercise)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.to_inbox_from_home)).perform(click());
        Thread.sleep(1000);
        //Reply to the exercise
        onView(withText("SelfReplyExercise")).perform(click()); //Replace SelfReplyExercise with "Reply Test" after #44 is merged
        onView(withText("Proud of you!")).perform(click());
        onView(withText("Send")).perform(click());
        //Navigate to Previous Activity -> Replies
        onView(withId(R.id.to_home_from_inbox)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.to_previous_activity_button)).perform(click());
        Thread.sleep(1000);
        onView(withText("SelfReplyExercise")).perform(click()); //Replace SelfReplyExercise with "Reply Test" after #44 is merged
        Thread.sleep(1000);
        //Check for correct reply from self
        onView(withText("UserR says \"Proud of you!\"")).perform(click());
    }

    @Test
    public void checkDeletedExerciseDeletion() throws InterruptedException {
        onView(withId(R.id.to_home_from_inbox)).perform(click());
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

        onView(withId(R.id.editTextExercise)).perform(typeText("Inbox delete exercise test"), closeSoftKeyboard());
        onView(withId(R.id.editTextNumberOfUnits)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.editTextUnits)).perform(typeText("Test"), closeSoftKeyboard());

        onView(withId(R.id.log_exercise_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_home_from_log_exercise)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());
        Thread.sleep(1000);

        onView(withText("Inbox delete exercise test")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.delete_exercise)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_home_from_previous_activity)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.to_inbox_from_home)).perform(click());
        Thread.sleep(1000);

        onView(withText("Inbox delete exercise test")).perform(click());
        Thread.sleep(1000);

        onView(withText("Send")).perform(click());
        Thread.sleep(1000);

        onView(withText("Inbox delete exercise test")).check(doesNotExist());
    }
}