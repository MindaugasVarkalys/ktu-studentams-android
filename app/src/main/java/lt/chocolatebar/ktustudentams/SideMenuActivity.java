package lt.chocolatebar.ktustudentams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SideMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final FragmentManager manager = getSupportFragmentManager();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        ScheduleFragment scheduleFragment = new ScheduleFragment();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setDefaultFragment(scheduleFragment);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        navigationView.setCheckedItem(R.id.nav_schedule);

    }

    protected void setDefaultFragment(Fragment fragment) {
        manager.beginTransaction().add(R.id.content_for_fragment, fragment).commit();
        toolbar.setTitle(R.string.drawer_schedule);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            ScheduleFragment scheduleFragment = new ScheduleFragment();
            manager.beginTransaction().
                    replace(R.id.content_for_fragment, scheduleFragment)
                    .commit();
            toolbar.setTitle(R.string.drawer_schedule);
        } else if (id == R.id.nav_grades) {
            GradesFragment gradesFragment = new GradesFragment();
            manager.beginTransaction()
                    .replace(R.id.content_for_fragment, gradesFragment)
                    .commit();
            toolbar.setTitle(R.string.drawer_grade);
        } else if (id == R.id.nav_choose_class_time) {
            ClassesPickerFragment classesPickerFragment = new ClassesPickerFragment();
            manager.beginTransaction()
                    .replace(R.id.content_for_fragment, classesPickerFragment)
                    .commit();
            toolbar.setTitle(R.string.drawer_choose_time_for_classes);
        } else if (id == R.id.nav_options) {
            OptionsFragment optionsFragment = new OptionsFragment();
            manager.beginTransaction()
                    .replace(R.id.content_for_fragment, optionsFragment)
                    .commit();
            toolbar.setTitle(R.string.drawer_options);
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
