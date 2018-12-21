package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import mx.com.vialogika.mistclient.Adapters.EdoGuardAdapter;
import mx.com.vialogika.mistclient.Pickers.DatePickerFragment;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.Provider;

public class GuardsEdoReports extends AppCompatActivity {

    private static final int CALENDAR_MENU_ID = 123456789;

    private List<String>          mSites      = new ArrayList<>();
    private List<Site>            sites       = new ArrayList<>();
    private List<String>          providers   = new ArrayList<>();
    private List<String>          grupos      = new ArrayList<>();
    private List<Provider>        proveedores = new ArrayList<>();
    private List<GuardForceState> edo         = new ArrayList<>();

    private RecyclerView               recyclerView;
    private RecyclerView.Adapter       rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int    siteid;
    private String from;
    private String to;
    private String currentSite;

    private Spinner menuSpinner, providerSpinner, groupSpinner;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> providerAdapter;
    private ArrayAdapter<String> groupAdapter;
    private DatabaseOperations   dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guards_edo_reports);
        init();
        getItems();
        getSiteList();
        getProviders();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem calendar = menu.add(Menu.NONE, CALENDAR_MENU_ID, Menu.NONE, "Elije fecha");
        calendar.setIcon(R.drawable.ic_date_range_black_24dp);
        calendar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        getMenuInflater().inflate(R.menu.edo_settings_menu, menu);
        setupmenu(menu);
        setupListeners();
        setSpinnerIfEnabled();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                syncEdo();
                break;
            case CALENDAR_MENU_ID:
                showDatePickerDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        from = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void getItems() {
        recyclerView = findViewById(R.id.edo_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter = new EdoGuardAdapter(edo);
        recyclerView.setAdapter(rvAdapter);
        providerSpinner = findViewById(R.id.spinnerProvider);
        groupSpinner = findViewById(R.id.spinnerGroup);
        providerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, providers);
        providerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grupos);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        providerSpinner.setAdapter(providerAdapter);
        groupSpinner.setAdapter(groupAdapter);
        dbo = new DatabaseOperations(this);
    }

    private void getProviders(){
        dbo.getProviders(new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                proveedores.addAll((List<Provider>) object);
            }
        });

    }

    private void getSortLists() {
        providers.clear();
        String pinit = "";
        String ginit = "";
        for (int i = 0; i < edo.size(); i++) {
            GuardForceState item         = edo.get(i);
            String          providerName = getProviderNameById(Integer.valueOf(item.getEdoFuerzaProviderId()));
            if (!providerName.equals(pinit)) {
                providers.add(providerName);
            }
            if (!item.getEdoFuerzaTurno().equals(ginit)) {
                grupos.add(item.getEdoFuerzaTurno());
            }
            pinit = providerName;
            ginit = item.getEdoFuerzaTurno();
        }
        setFilterSpinnersIfEnabled();
        providerAdapter.notifyDataSetChanged();
        groupAdapter.notifyDataSetChanged();
    }

    private void setFilterSpinnersIfEnabled() {
        if (providers.size() <= 1) {
            providerSpinner.setEnabled(false);
            groupSpinner.setEnabled(false);
        } else {
            providerSpinner.setEnabled(true);
            groupSpinner.setEnabled(true);
        }
    }

    private String getProviderNameById(int id) {
        for (int i = 0; i < proveedores.size(); i++) {
            Provider cur = proveedores.get(i);
            if (cur.getProviderId() == id) {
                return cur.getProviderAlias();
            }
        }
        return "Provedor desconocido";
    }

    private void setupmenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        menuSpinner = (Spinner) item.getActionView();
        menuSpinner.setAdapter(adapter);
    }

    private void getSiteList() {
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null) {
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

    private void setSpinnerIfEnabled() {
        if (mSites.size() == 1) {
            if (menuSpinner != null) {
                menuSpinner.setEnabled(false);
            }
        }
    }

    private void setupListeners() {
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSite = menuSpinner.getSelectedItem().toString();
                siteid = sites.get(position).getSiteId();
                loadEdo();
                Toast.makeText(GuardsEdoReports.this, "Site id : " + siteid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadEdo() {
        dbo.getEdoReports(from, to, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null) {
                    edo.clear();
                    edo.addAll((List<GuardForceState>) object);
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                rvAdapter.notifyDataSetChanged();
                getSortLists();
            }
        });
    }

    private void syncEdo() {
        NetworkRequest.getEdoFuerza(this, from, to, siteid, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                edo.clear();
                edo.addAll((List<GuardForceState>) object);
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                loadEdo();
            }
        });
    }

    private void showDatePickerDialog() {
        DialogFragment datepicker = new DatePickerFragment();
        ((DatePickerFragment) datepicker).setCallback(new DatePickerFragment.DatePicker() {
            @Override
            public void onDateSet(Date datefrom, Date dateto) {
                from = new SimpleDateFormat("yyyy-MM-dd").format(datefrom);
                to = new SimpleDateFormat("yyyy-MM-dd").format(dateto);
                loadEdo();
            }
        });
        datepicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateDates() {

    }
}
