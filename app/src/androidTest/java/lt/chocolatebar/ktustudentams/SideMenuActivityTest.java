package lt.chocolatebar.ktustudentams;


import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import lt.chocolatebar.ktustudentams.activities.SideMenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SideMenuActivityTest {

    private static final String PACKAGE_NAME = "com.android.chrome";

    @Rule
    public IntentsTestRule<SideMenuActivity> mActivityRule = new IntentsTestRule<>(
            SideMenuActivity.class);

    @Test
    public void OpenDrawer_selectScheduleItem_displayFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_schedule));
        onView(withId(R.id.schedule_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void OpenDrawer_selectGradesItem_displayFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_grades));
        onView(withId(R.id.grades_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void OpenDrawer_selectClassPickerItem_displayFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_choose_class_time));
        onView(withId(R.id.classes_picker_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void OpenDrawer_selectOptionsItem_displayFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_options));
        onView(withText(R.string.notification_settings)).check(matches(isDisplayed()));
    }

    @Test
    public void OpenDrawer_selectMoodleItem_displayWebsite2() throws Exception {
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData("https://moodle.ktu.edu/"));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_moodle));
        intended(expectedIntent);
    }

    @Test
    public void OpenDrawer_selectLogoutItem_displayFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));
    }
}
