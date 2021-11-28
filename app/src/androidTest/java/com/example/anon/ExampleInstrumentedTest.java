package com.example.anon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import kotlin.jvm.JvmField;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.anon", appContext.getPackageName());
    }

    @Test
    public void testUpvote(){

    }
//
//    @RunWith(AndroidJUnit4.class)
//    @LargeTest
//    public class ChangeTextBehaviorTest {

    private String stringToBetyped;

    @Before
    public void setUp() {
//        EspressoIdlingResource res = new EspressoIdlingResource();
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
//        onView(withId(R.id.upVoteBtn))
//                .perform(typeText(stringToBetyped), closeSoftKeyboard());
        try {
            Thread.sleep(5000);
        }catch (Exception e){}
        onView(withIndex(withId(R.id.upVoteBtn), 0)).perform(click());

        // Check that the text was changed.
        onView(withIndex(withId(R.id.numVotesText), 0))
                .check(matches(withText("36")));
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource);
    }
//    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

}
class EspressoIdlingResource {

    @JvmField

    private static String CLASS_NAME = "EspressoIdlingResource";

    private static String RESOURCE = "GLOBAL";
    public static CountingIdlingResource countingIdlingResource = new CountingIdlingResource(RESOURCE);

    public EspressoIdlingResource(){
        countingIdlingResource = new CountingIdlingResource(RESOURCE);
    }

    void increment() {
        countingIdlingResource.increment();
    }

    void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }
}