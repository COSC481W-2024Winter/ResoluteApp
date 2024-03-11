package com.example.resoluteapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Register_IT {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigateToRegisterScreen() throws InterruptedException {
        onView(withId(R.id.to_register_button)).perform(click());
        Thread.sleep(2000);
    }

    @Test
    public void usernameTaken() throws InterruptedException{
        //Fill in textboxes
        onView(withId(R.id.register_username_entry)).perform(typeText("User1"), closeSoftKeyboard());
        onView(withId(R.id.register_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.register_email_entry)).perform(typeText("justanotherfakeemailaddress@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.register_name_entry)).perform(typeText("User's name"), closeSoftKeyboard());

        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());
    }

    @Test
    public void emailTaken() throws InterruptedException{
        //Fill in textboxes
        onView(withId(R.id.register_username_entry)).perform(typeText("BrandNewUsername"), closeSoftKeyboard());
        onView(withId(R.id.register_password_entry)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.register_email_entry)).perform(typeText("user1@resolute.com"), closeSoftKeyboard());
        onView(withId(R.id.register_name_entry)).perform(typeText("User's name"), closeSoftKeyboard());

        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());
    }

    @Test
    public void checkEmptyFields() throws InterruptedException{
        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);
        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());

        //Fill in username textbox
        onView(withId(R.id.register_username_entry)).perform(typeText("BrandNewUsername"), closeSoftKeyboard());
        //Fill in password textbox
        onView(withId(R.id.register_password_entry)).perform(typeText("Password"), closeSoftKeyboard());
        //Fill in a third textbox
        onView(withId(R.id.register_email_entry)).perform(typeText("justanotherfakeemailaddress@gmail.com"), closeSoftKeyboard());
        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);
        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());

        //This test should always confirm that registration fails based on an empty textbox, since
        //First Name is the only textbox with no outstanding requirements besides being filled in.
    }

    @Test
    public void passwordLength() throws InterruptedException{
        //Fill in textboxes
        onView(withId(R.id.register_username_entry)).perform(typeText("BrandNewUser"), closeSoftKeyboard());
        onView(withId(R.id.register_password_entry)).perform(typeText("2Short"), closeSoftKeyboard());
        onView(withId(R.id.register_email_entry)).perform(typeText("justanotherfakeemailaddress@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.register_name_entry)).perform(typeText("User's name"), closeSoftKeyboard());

        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());
    }

    @Test
    public void emailAtSign() throws InterruptedException{
        //Fill in textboxes
        onView(withId(R.id.register_username_entry)).perform(typeText("BrandNewUser"), closeSoftKeyboard());
        onView(withId(R.id.register_password_entry)).perform(typeText("Password"), closeSoftKeyboard());
        onView(withId(R.id.register_email_entry)).perform(typeText("fakeemailaddress_gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.register_name_entry)).perform(typeText("User's name"), closeSoftKeyboard());

        //Click register
        onView(withId(R.id.register_button)).perform(click());
        Thread.sleep(2000);

        //Check that element from login fragment is not accessible, ensuring register failed and RegisterFragment is still active
        onView(withId(R.id.login_button)).check(doesNotExist());
    }
}
