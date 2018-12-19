package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.vialogika.mistclient.Pickers.DatePickerFragment;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;

public class GuardsEdoReports extends AppCompatActivity {

    private List<String> mSites = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private List<GuardForceState> edo = new ArrayList<>();
    private int siteid;
    private String from = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    private Spinner menuSpinner;
    private ArrayAdapter<String> adapter;
    private DatabaseOperations dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guards_edo_reports);
        init();
        getSiteList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edo_settings_menu,menu);
        setupmenu(menu);
        setupListeners();
        setSpinnerIfEnabled();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                //getSiteList();
                showDatePickerDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mSites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dbo = new DatabaseOperations(this);
    }

    private void setupmenu(Menu menu){
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        menuSpinner = (Spinner) item.getActionView();
        menuSpinner.setAdapter(adapter);
    }

    private void getSiteList(){
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    sites.clear();
                    sites.addAll((List<Site>) object);
                }
                mSites.clear();
                for (int i = 0; i < sites.size(); i++) {
                    Site site = sites.get(i);
                    mSites.add(site.getSiteName());
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setSpinnerIfEnabled(){
        if (mSites.size() == 1){
            if (menuSpinner != null){
                menuSpinner.setEnabled(false);
            }
        }
    }

    private void setupListeners(){
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                siteid = sites.get(position).getSiteId();
                loadEdo();
                Toast.makeText(GuardsEdoReports.this, "Site id : " + siteid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadEdo(){
        dbo.getEdoReports(from, to, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    edo.clear();
                    edo.addAll((List<GuardForceState>) object);
                    if (edo.size() < 1){
                        loadEdoReportsFromNetwork();
                    }
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {

            }
        });
    }

    private void loadEdoReportsFromNetwork(){
        NetworkRequest.getEdoFuerza(this, from, to, siteid, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                edo.clear();
                edo.addAll((List<GuardForceState>) object);
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {

            }
        });
    }

    private void showDatePickerDialog(){
        DialogFragment datepicker = new DatePickerFragment();
        ((DatePickerFragment) datepicker).setCallback(new DatePickerFragment.DatePicker() {
            @Override
            public void onDateSet(Date datefrom, Date dateto) {
                from = new SimpleDateFormat("yyyy-MM-dd").format(datefrom);
                to = new SimpleDateFormat("yyyy-MM-dd").format(dateto);
                loadEdo();
            }
        });
        datepicker.show(getSupportFragmentManager(),"datePicker");
    }

    private void updateDates(){

    }
}
