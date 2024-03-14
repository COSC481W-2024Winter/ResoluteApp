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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

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
        // Ensure the table is displayed
        onView(withId(R.id.requestTable)).check(matches(isDisplayed()));

        // Store the initial number of rows in the table
        final int[] initialRowCount = new int[1];
        onView(withId(R.id.requestTable)).check((view, noViewFoundException) -> {
            TableLayout tableLayout = (TableLayout) view;
            // Assuming there is a header row that you want to exclude from the count
            initialRowCount[0] = tableLayout.getChildCount() - 1;
        });

        // Specifically target the first visible "Deny" button that is a direct child of a TableRow
        onView(allOf(withText(R.string.deny), withParent(instanceOf(TableRow.class)), instanceOf(Button.class), isDisplayed())).perform(click());

        // Check if the row count decreased by 1 after clicking "Deny"
        onView(withId(R.id.requestTable)).check((view, noViewFoundException) -> {
            TableLayout tableLayout = (TableLayout) view;
            int rowCountAfterDenial = tableLayout.getChildCount() - 1; // Assuming there is a header row
            assertEquals("Row should be deleted", initialRowCount[0] - 1, rowCountAfterDenial);
        });
    }


    @Test
    public void testRowDeletionOnApprove() {

    }

}
