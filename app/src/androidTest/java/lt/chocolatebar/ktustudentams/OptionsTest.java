package lt.chocolatebar.ktustudentams;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import lt.chocolatebar.ktustudentams.activities.EmptyActivityTest;
import lt.chocolatebar.ktustudentams.fragments.OptionsFragment;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OptionsTest {
    @Rule
    public ActivityTestRule<EmptyActivityTest> emptyAct = new ActivityTestRule<>(EmptyActivityTest.class);
    OptionsFragment frag;

    @Before
    public void setUp() throws Exception {
        frag  = new OptionsFragment();

        emptyAct.getActivity().getFragmentManager().beginTransaction().add(0, frag, "TAG").commit();
    }

    @Test
    public void test() throws Exception {

    }

}
