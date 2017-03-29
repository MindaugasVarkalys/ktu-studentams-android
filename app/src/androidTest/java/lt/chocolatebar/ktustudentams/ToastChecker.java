package lt.chocolatebar.ktustudentams;

import android.support.test.rule.ActivityTestRule;
import android.widget.Toast;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ToastChecker {

    public static void assertToastDisplayed(int toastStringId, ActivityTestRule activityRule) throws Exception {
        onView(withText(toastStringId)).
                inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        Thread.sleep(3500);
    }

}
