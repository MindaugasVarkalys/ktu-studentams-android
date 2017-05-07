package lt.chocolatebar.ktustudentams.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;

import lt.chocolatebar.ktustudentams.R;

public class OptionsFragment extends PreferenceFragment {

    public OptionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        addPreferencesFromResource(R.xml.fragment_options);
        getActivity().setTitle(R.string.settings);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_options);
    }
}
