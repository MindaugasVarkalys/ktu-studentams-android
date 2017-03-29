package lt.chocolatebar.ktustudentams;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static lt.chocolatebar.ktustudentams.ToastChecker.assertToastDisplayed;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivity = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void clickLoginWithEmptyUsernameAndPassword_displaysToastAndFocusUsername() throws Exception {
        onView(withId(R.id.login)).perform(click());
        assertToastDisplayed(R.string.username_error_message, loginActivity);
        onView(withId(R.id.username)).check(matches(hasFocus()));
    }

    @Test
    public void clickLoginWithEmptyUsername_displaysToastAndFocusUsername() throws Exception {
        onView(withId(R.id.password)).perform(typeText("Password"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        assertToastDisplayed(R.string.username_error_message, loginActivity);
        onView(withId(R.id.username)).check(matches(hasFocus()));
    }

    @Test
    public void clickLoginWithEmptyPassword_displaysToastAndFocusPassword() throws Exception {
        onView(withId(R.id.username)).perform(typeText("Username"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        assertToastDisplayed(R.string.password_error_message, loginActivity);
        onView(withId(R.id.password)).check(matches(hasFocus()));
    }
}
