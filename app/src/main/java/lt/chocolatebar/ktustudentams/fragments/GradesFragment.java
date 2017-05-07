package lt.chocolatebar.ktustudentams.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import lt.chocolatebar.ktustudentams.LoadingDialog;
import lt.chocolatebar.ktustudentams.R;
import lt.chocolatebar.ktustudentams.data.Semester;
import lt.chocolatebar.ktustudentams.network.NetworkUtils;
import lt.chocolatebar.ktustudentams.network.SemestersScraper;

public class GradesFragment extends Fragment {

    public GradesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_grade);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_grades);
        scrapSemestersAndModules();
    }

    private void scrapSemestersAndModules() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            LoadingDialog.show(getActivity());
            SemestersScraper scraper = new SemestersScraper();
            scraper.setOnSemestersScrapedListener(this::onSemestersScraped);
            scraper.scrap();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSemestersScraped(@Nullable List<Semester> semesters) {
        LoadingDialog.dismiss();
        if (semesters != null) {
            Log.e("Nuscrapino", "JEGA");
        } else {
            Toast.makeText(getActivity(), R.string.failure, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }
}
