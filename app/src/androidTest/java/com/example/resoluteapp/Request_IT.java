package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

import android.widget.TableLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Request_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToLogExerciseScreen() throws InterruptedException {
        // Navigate to the log exercise screen before each test
        onView(withId(R.id.login_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // You may need to add a delay here if navigation is asynchronous
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
        onView(withId(R.id.friends_identifier)).check(matches(isDisplayed()));

    }

    @Test
    public void testRowDeletionOnDeny() {

        onView(withId(R.id.requestTable)).check(matches(isDisplayed()));

        final int[] initialRowCount = new int[1];
        onView(withId(R.id.requestTable)).check((view, noViewFoundException) -> {
            TableLayout tableLayout = (TableLayout) view;
            initialRowCount[0] = tableLayout.getChildCount() - 1; // Subtract header row
        });

        onView(allOf(withText(R.string.deny), isDisplayed())).perform(click());

        onView(withId(R.id.requestTable)).check((view, noViewFoundException) -> {
            TableLayout tableLayout = (TableLayout) view;
            int rowCountAfterDenial = tableLayout.getChildCount() - 1; // Subtract header row
            assert rowCountAfterDenial == initialRowCount[0] - 1;
        });


    }

    @Test
    public void testRowDeletionOnApprove() {

    }

}
