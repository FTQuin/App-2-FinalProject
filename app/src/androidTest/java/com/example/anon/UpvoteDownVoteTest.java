package com.example.anon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.anon", appContext.getPackageName());
//    }
//
//    @Test
//    public void testUpvote() {
//
//    }
//}

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpvoteDownVoteTest {
    EspressoIdlingResource eir = new EspressoIdlingResource();
    String initialNumString;
    int initialNum;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(eir.countingIdlingResource);
        //sleep to wait firebase to load
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    @Test
    public void upVoteTest() {
        initialNumString = getText(onView(withIndex(withId(R.id.numVotesText), 0)));
        initialNum = Integer.parseInt(initialNumString);

        // click upvote
        onView(withIndex(withId(R.id.upVoteBtn), 0)).perform(click());

        // Check that the text was changed.
        onView(withIndex(withId(R.id.numVotesText), 0))
                .check(matches(withText(String.valueOf(initialNum + 1))));
    }

    @Test
    public void downVoteTest() {
        initialNumString = getText(onView(withIndex(withId(R.id.numVotesText), 0)));
        initialNum = Integer.parseInt(initialNumString);

        // click down vote
        onView(withIndex(withId(R.id.downVoteBtn), 0)).perform(click());

        // Check that the text was changed.
        onView(withIndex(withId(R.id.numVotesText), 0))
                .check(matches(withText(String.valueOf(initialNum - 1))));
    }

    @Test
    public void commentTest(){
        String testComment = "JUnit Test Comment.";

        onView(withIndex(withId(R.id.postCardView), 0)).perform(click());

        onView(withIndex(withId(R.id.txtComment), 0))
                .perform(typeText(testComment));
        onView(withId(R.id.postCommentBtn)).perform(click());

        // Check that the text was added.
        onView(withIndex(withId(R.id.content), 0))
                .check(matches(withText(testComment)));
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(eir.countingIdlingResource);
    }


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

    private String getText(ViewInteraction matcher) {
        final String[] text = new String[1];
        matcher.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Text of the view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                text[0] = tv.getText().toString();
            }
        });

        return text[0];
    }

}

