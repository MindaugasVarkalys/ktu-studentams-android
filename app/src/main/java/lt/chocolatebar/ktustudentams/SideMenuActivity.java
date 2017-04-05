package lt.chocolatebar.ktustudentams;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import lt.chocolatebar.ktustudentams.fragments.ClassesPickerFragment;
import lt.chocolatebar.ktustudentams.fragments.GradesFragment;
import lt.chocolatebar.ktustudentams.fragments.OptionsFragment;
import lt.chocolatebar.ktustudentams.fragments.ScheduleFragment;

public class SideMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String KTU_MOODLE_URL = "https://moodle.ktu.edu/";

    private final FragmentManager manager = getFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDefaultFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setDefaultFragment() {
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        Class<? extends Fragment> fragmentClass = sharedPrefs.getDefaultFragmentClass();
        try {
            Fragment fragment = fragmentClass.newInstance();
            manager.beginTransaction().add(R.id.content_for_fragment, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openMoodleInBrowser() {
        Uri uriUrl = Uri.parse(KTU_MOODLE_URL);
        Intent launchBrower = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrower);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_schedule:
                fragment = new ScheduleFragment();
                break;
            case R.id.nav_grades:
                fragment = new GradesFragment();
                break;
            case R.id.nav_choose_class_time:
                fragment = new ClassesPickerFragment();
                break;
            case R.id.nav_options:
                fragment = new OptionsFragment();
                break;
            case R.id.nav_moodle:
                openMoodleInBrowser();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.content_for_fragment, fragment)
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
