package lt.chocolatebar.ktustudentams;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import lt.chocolatebar.ktustudentams.data.User;
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

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putStringSet("user", user.toStringSet());
        editor.apply();
    }

    public User getUser() {
        return User.getFromStringSet(sharedPrefs.getStringSet("user", null));
    }

    public void deleteUser() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("user");
        editor.apply();
    }
}
