package com.example.himanshuaggarwal.contactlist;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.content.Intent.ACTION_DIAL;
import static android.content.Intent.ACTION_SENDTO;
import static org.junit.Assert.*;

/**
 * Created by himanshuaggarwal on 03/04/18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListContact_A2_MT17015Test {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Rule
//    public ActivityTestRule<ListContact_A2_MT17015> mActivityRule =
//            new ActivityTestRule<ListContact_A2_MT17015>(ListContact_A2_MT17015.class);

    public IntentsTestRule<ListContact_A2_MT17015> mActivityRule =
            new IntentsTestRule<ListContact_A2_MT17015>(ListContact_A2_MT17015.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.himanshuaggarwal.contactlist", appContext.getPackageName());
    }

    @Test
    public void checkButtonClick(){
        onView(withId(R.id.add_button)).check(matches(isClickable()));
    }

    @Test
    public void checkRecylerView(){
        onView(withId(R.id.contact_recycler_view)).check(matches(hasDescendant(withText("ambulance"))));
    }

    @Test
    public void checkDetailView(){
        onView(withId(R.id.contact_recycler_view)).perform(click());
        onView(withId(R.id.edit_button)).check(matches(isClickable()));
    }

    @Test
    public void checkCallAction(){
        onView(withId(R.id.contact_recycler_view)).perform(click());
        onView(withId(R.id.detail_contact_number)).perform(click());
        intended(hasAction(ACTION_DIAL));
    }
}