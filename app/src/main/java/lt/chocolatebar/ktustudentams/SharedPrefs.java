package lt.chocolatebar.ktustudentams;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import lt.chocolatebar.ktustudentams.fragments.ClassesPickerFragment;
import lt.chocolatebar.ktustudentams.fragments.GradesFragment;
import lt.chocolatebar.ktustudentams.fragments.ScheduleFragment;

public class SharedPrefs {

    private Context context;
    private SharedPreferences sharedPrefs;

    public SharedPrefs(Activity activity) {
        context = activity;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public Class<? extends Fragment> getDefaultFragmentClass() {
        String defaultFragment = sharedPrefs.getString("list_pref_default_fragment", "");
        if (defaultFragment.equals(context.getString(R.string.drawer_grade))) {
            return GradesFragment.class;
        } else if (defaultFragment.equals(context.getString(R.string.drawer_choose_time_for_classes))) {
            return ClassesPickerFragment.class;
        } else {
            return ScheduleFragment.class;
        }
    }
}
