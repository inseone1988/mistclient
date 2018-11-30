package mx.com.vialogika.mistclient;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.EdoPagerAdapter;

public class EdoSettings extends AppCompatActivity implements EdoClients.OnFragmentInteractionListener, EdoGuards.OnFragmentInteractionListener, EdoApostamientos.OnFragmentInteractionListener,EdoProviders.OnFragmentInteractionListener {

    private Spinner menuSpinner;
    private List<String> mSites = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edo_settings);
        init();
        getSiteList();
        setupTabs();
        setActionBarTitle();
    }

    private void init() {
        adapter = new ArrayAdapter<>(EdoSettings.this, android.R.layout.simple_spinner_item, mSites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupTabs() {
        EdoPagerAdapter edoPagerAdapter = new EdoPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.edo_view_pager);
        pager.setAdapter(edoPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.edo_settings_menu, menu);
        setupOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_refresh:
                updateAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle() {
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Intramuros");
    }

    private void setupOptionsMenu(final Menu menu) {
        smenu(menu);
    }

    private void getSiteList() {
        DatabaseOperations dbo = new DatabaseOperations(this);
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    sites.addAll((List<Site>) object);
                }
                for (int i = 0; i < sites.size(); i++) {
                    Site site = sites.get(i);
                    mSites.add(site.getSiteName());
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                updateAdapter();
            }
        });
    }

    private void updateAdapter(){
        adapter.notifyDataSetChanged();
    }

    private void smenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        menuSpinner = (Spinner) item.getActionView();
        menuSpinner.setAdapter(adapter);
        updateAdapter();
    }

    private int getSiteId(String sitename){
        for (int i = 0; i< sites.size();i++){
            if (sites.get(i).getSiteName().equals(sitename)){
                return sites.get(i).getSiteId();
            }
        }
        return 0;
    }


    /**This events will be handled inside each fragment
    private void setSpinnerOnSiteSelected() {
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = menuSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
