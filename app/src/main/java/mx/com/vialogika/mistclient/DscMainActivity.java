package mx.com.vialogika.mistclient;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.android.job.JobManager;

import org.json.JSONObject;

import mx.com.vialogika.mistclient.Notif.AppNotifications;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Utils.AppJobCreator;
import mx.com.vialogika.mistclient.Utils.Dialogs;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.ReportSyncJob;
import mx.com.vialogika.mistclient.Utils.SimpleDialogCallback;

public class DscMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ReportsFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView usernamefield;
    private TextView mailfield;
    private ImageView profileImage;

    private String username;
    private String userd;

    private DatabaseOperations dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JobManager.create(this).addJobCreator(new AppJobCreator());
        ReportSyncJob.scheduleJob();
        setContentView(R.layout.activity_dsc_main);
        initdb();
        getSPValues();
        getItems();
        setSupportActionBar(toolbar);
        setupActionListeners();
        setupDrawer();
        loadFragment(new ReportsFragment());
        navigationView.setNavigationItemSelectedListener(this);
        handleIntent();
        //showSampleNotification();
    }

    private void initdb(){
        dbo = new DatabaseOperations(this);
    }

    private void getItems(){
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Inicio");
    }

    private void setupDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationView navview = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navview.getHeaderView(0);
                usernamefield = headerView.findViewById(R.id.drawer_user_name);
                mailfield = findViewById(R.id.drawer_user_mail);
                updateUserData();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void showSampleNotification(){
        AppNotifications mNotifs = new AppNotifications(this);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(AppNotifications.TESTIN_NOTIFICATION_HANDLE,mNotifs.testingNotofication().build());
    }

    private void handleIntent(){
        Intent intent = getIntent();
        if (intent.hasExtra("loadReportsFragment")){
            loadFragment(new ReportsFragment());
        }
    }

    private void setupActionListeners(){

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void updateUserData(){
        usernamefield.setText(username);
        mailfield.setText(userd);
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fregment_holder,fragment);
        ft.commit();
    }

    private void getSPValues(){
        SharedPreferences sp = getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        username = sp.getString("user_fullname","Supply Security User");
        userd = sp.getString("user_login","@someuser");
    }

    private void enableLogin(){
        SharedPreferences sp = getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.should_auto_open),false);
        editor.apply();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.addSubMenu("Hello");
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dsc_main, menu);
        return true;
    }

    private void sampleToast(){
        Toast.makeText(this,R.string.example_text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sampleToast();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void finishApp(){
        finish();
    }

    private void updateAppBarTitle(int fragmentid){
        int appBarTitle = R.string.title_activity_log_in;
        switch(fragmentid){
            case R.id.nav_reports:
                appBarTitle = R.string.appbar_title_reports;
                break;
        }
        toolbar.setTitle(appBarTitle);
    }

    private void clearBeforeExit(){
        enableLogin();
        dbo.deleteAllTables();
        JobManager.instance().cancelAll();
        User.clearUserSP(this);
        NetworkRequest.deleteGuardPictures(this);
        finishApp();
    }

    private void doOnBackground(final runTaskCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                cb.doInBackgroung();
            }
        }).start();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_reports:
                    loadFragment(new ReportsFragment());
                break;
            case R.id.nav_edo:
                loadFragment(new EdoFragment());
                break;
            case R.id.nav_exit_app:
                Dialogs.exitAppDialog(this, new SimpleDialogCallback() {
                    @Override
                    public void AccountStayOpen() {
                        clearBeforeExit();
                    }
                });
                break;
        }

        updateAppBarTitle(id);
        /**if (id == R.id.nav_reports) {
            // Handle the camera action
        } else if (id == R.id.nav_edo) {

        } else if (id == R.id.nav_stadistics) {

        } else if (id == R.id.nav_exit_app) {
            Dialogs.exitAppDialog(this, new SimpleDialogCallback() {
                @Override
                public void AccountStayOpen() {
                    enableLogin();
                    finishApp();
                }
            });

        } else if (id == R.id.nav_bug_report) {

        } else if (id == R.id.nav_about) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface runTaskCallback{
        void doInBackgroung();
    }
}
