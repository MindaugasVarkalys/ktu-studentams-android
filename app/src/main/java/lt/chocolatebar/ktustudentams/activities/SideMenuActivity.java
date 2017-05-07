package lt.chocolatebar.ktustudentams.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import lt.chocolatebar.ktustudentams.R;
import lt.chocolatebar.ktustudentams.SharedPrefs;
import lt.chocolatebar.ktustudentams.fragments.ClassesPickerFragment;
import lt.chocolatebar.ktustudentams.fragments.GradesFragment;
import lt.chocolatebar.ktustudentams.fragments.OptionsFragment;
import lt.chocolatebar.ktustudentams.fragments.ScheduleFragment;

public class SideMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String KTU_MOODLE_URL = "https://moodle.ktu.edu/";

    private final FragmentManager manager = getFragmentManager();
    private SharedPrefs sharedPrefs;
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    private boolean firstTimeGrouping = true;
    private final int notificationDisplaysArray[] = new int[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDefaultFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sharedPrefs = new SharedPrefs(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setStudentInfoInNavigationHeader(navigationView);

        onNewIntent(getIntent());
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
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void setStudentInfoInNavigationHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView navStudentNameLastname = (TextView) headerView.findViewById(R.id.StudentNameLastname);
        TextView navStudentCode = (TextView) headerView.findViewById(R.id.StudentCode);
        navStudentNameLastname.setText(sharedPrefs.getUser().getName() + " " + sharedPrefs.getUser().getSurname());
        navStudentCode.setText(sharedPrefs.getUser().getCode());
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

    private void generateAndDisplayNotificationForGrades() {
        notificationDisplaysArray[0] = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = buildNotificationForGrades();

        Intent intent = new Intent(this, SideMenuActivity.class);
        intent.putExtra("ShowGradesFragment", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (notificationDisplaysArray[1] == 1) {
            inboxStyle.setBigContentTitle(getString(R.string.app_name));
            if (firstTimeGrouping) {
                inboxStyle.addLine("Naujas užsirašymo laikas!");
                firstTimeGrouping = false;
            }
            inboxStyle.addLine("Naujas pažymys!");
            notificationBuilder.setStyle(inboxStyle);
            notificationManager.cancel(1);
        }
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private NotificationCompat.Builder buildNotificationForGrades() {
        return (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_grade)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Naujas pažymys!")
                .setContentText("Some Text")
                .setStyle(new NotificationCompat.MediaStyle())
                .setAutoCancel(true);
    }

    private void generateAndDisplayNotificationForClassPicker() {
        notificationDisplaysArray[1] = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = buildNotificationForClassPicker();

        Intent intent = new Intent(this, SideMenuActivity.class);
        intent.putExtra("ShowClassPickerActivity", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (notificationDisplaysArray[0] == 1) {
            inboxStyle.setBigContentTitle(getString(R.string.app_name));
            if (firstTimeGrouping) {
                inboxStyle.addLine("Naujas pažymys!");
                firstTimeGrouping = false;
            }
            inboxStyle.addLine("Naujas užsirašymo laikas!");
            notificationBuilder.setStyle(inboxStyle);
            notificationManager.cancel(0);
        }
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private NotificationCompat.Builder buildNotificationForClassPicker() {
        return (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_assignment)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Naujas užsirašymo laikas!")
                .setContentText("Some Text")
                .setStyle(new NotificationCompat.MediaStyle())
                .setAutoCancel(true);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            boolean loadFragmentFromNotification = extras.getBoolean("ShowGradesFragment", false);
            if (loadFragmentFromNotification) {
                Fragment fragment = new GradesFragment();
                manager.beginTransaction()
                        .replace(R.id.content_for_fragment, fragment)
                        .commit();
                ResetNotificationStatus(0);
            } else {
                Fragment fragment = new ClassesPickerFragment();
                manager.beginTransaction()
                        .replace(R.id.content_for_fragment, fragment)
                        .commit();
                ResetNotificationStatus(1);
            }
        }
    }

    private void ResetNotificationStatus(int id) {
        if (notificationDisplaysArray[0] == 1 && notificationDisplaysArray[1] == 1) {
            notificationDisplaysArray[0] = 0;
            notificationDisplaysArray[1] = 0;
            firstTimeGrouping = true;
            inboxStyle = new NotificationCompat.InboxStyle();
        } else notificationDisplaysArray[id] = 0;
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
                generateAndDisplayNotificationForGrades();
                break;
            case R.id.nav_choose_class_time:
                fragment = new ClassesPickerFragment();
                generateAndDisplayNotificationForClassPicker();
                break;
            case R.id.nav_options:
                fragment = new OptionsFragment();
                break;
            case R.id.nav_moodle:
                openMoodleInBrowser();
                break;
            case R.id.nav_logout:
                onLogout();
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

    private void onLogout() {
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        sharedPrefs.deleteUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
