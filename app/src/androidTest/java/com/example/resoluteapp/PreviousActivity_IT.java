package com.example.resoluteapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;

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
public class PreviousActivity_IT {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);




    @Before
    public void navigateToPreviousActivityPage() throws InterruptedException {
        onView(withId(R.id.login_username_entry)).perform(typeText("Test_User1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.to_previous_activity_button)).perform(click());

        Thread.sleep(1000);
    }

    @Test
    public void navigationBackHomeTest() throws InterruptedException {
        onView(withId(R.id.to_home_from_previous_activity)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.welcome_message)).check(matches(withText("Hello " + "User1" + "\nWelcome to Resolute!")));
    }

    @Test
    public void orderedTableTest() throws InterruptedException {
        onView(withId(R.id.tableLayout)).check(matches(isDisplayed()));

        onView(allOf(withText(R.id.date),
                isDescendantOfA(allOf(instanceOf(TableRow.class), isDisplayed()))));

    }

    @Test
    public void currentUserTest() throws InterruptedException{



    }

}

