package lt.chocolatebar.ktustudentams.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lt.chocolatebar.ktustudentams.LoadingDialog;
import lt.chocolatebar.ktustudentams.R;
import lt.chocolatebar.ktustudentams.data.Module;
import lt.chocolatebar.ktustudentams.data.Semester;
import lt.chocolatebar.ktustudentams.data.Week;
import lt.chocolatebar.ktustudentams.network.GradesScraper;
import lt.chocolatebar.ktustudentams.network.NetworkUtils;
import lt.chocolatebar.ktustudentams.network.SemestersScraper;

public class GradesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner semesterSpinner, moduleSpinner;
    private TableLayout table;
    private List<Semester> semesters;

    public GradesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.weeks);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_grades);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        table = (TableLayout) view.findViewById(R.id.grades_table);
        semesterSpinner = (Spinner) view.findViewById(R.id.semesters);
        semesterSpinner.setOnItemSelectedListener(this);
        moduleSpinner = (Spinner) view.findViewById(R.id.modules);
        moduleSpinner.setOnItemSelectedListener(this);
        scrapSemestersAndModules();
    }

    private void onSemesterSelectionChanged(int position) {
        setSpinnerItems(moduleSpinner, getModulesNames(semesters.get(position).getModules()));
    }

    private String[] getModulesNames(List<Module> modules) {
        String[] names = new String[modules.size()];
        for (int i = 0; i < modules.size(); i++) {
            names[i] = modules.get(i).getCode() + " " + modules.get(i).getName();
        }
        return names;
    }

    private void onModuleSelectionChanged(int position) {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            LoadingDialog.show(getActivity());
            Module module = semesters.get(semesterSpinner.getSelectedItemPosition()).getModules().get(position);
            GradesScraper scraper = new GradesScraper();
            scraper.setOnGradesScrapedListener(this::onGradesScraped);
            scraper.scrap(module);
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerItems(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
            this.semesters = semesters;
            setSpinnerItems(semesterSpinner, getSemestersNames(semesters));
        } else {
            Toast.makeText(getActivity(), R.string.failure, Toast.LENGTH_SHORT).show();
        }
    }

    private String[] getSemestersNames(List<Semester> semesters) {
        String[] semesterNames = new String[semesters.size()];
        for (int i = 0; i < semesters.size(); i++) {
            semesterNames[i] = semesters.get(i).getName();
        }
        return semesterNames;
    }

    private void onGradesScraped(@Nullable Module module) {
        LoadingDialog.dismiss();
        if (module != null && module.getWeeks() != null) {
            buildTable(module);
        } else {
            Toast.makeText(getActivity(), R.string.failure, Toast.LENGTH_SHORT).show();
        }
    }

    private void buildTable(Module module) {
        table.removeViews(1, table.getChildCount() - 1);
        for (Week week : module.getWeeks()) {
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.grades_table_row, null);
            TextView weekView = (TextView) row.findViewById(R.id.week);
            TextView name = (TextView) row.findViewById(R.id.name);
            TextView firstGrade = (TextView) row.findViewById(R.id.grade1);
            TextView secondGrade = (TextView) row.findViewById(R.id.grade2);
            TextView thirdGrade = (TextView) row.findViewById(R.id.grade3);

            weekView.setText(week.getWeek());
            name.setText(week.getShortName());
            firstGrade.setText(week.getFirstGrade());
            secondGrade.setText(week.getSecondGrade());
            thirdGrade.setText(week.getThirdGrade());
            table.addView(row);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.semesters:
                onSemesterSelectionChanged(position);
                break;
            case R.id.modules:
                onModuleSelectionChanged(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
