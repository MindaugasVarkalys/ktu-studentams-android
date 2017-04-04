package lt.chocolatebar.ktustudentams.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import lt.chocolatebar.ktustudentams.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends PreferenceFragment {


    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        addPreferencesFromResource(R.xml.fragment_options);


    }


}
