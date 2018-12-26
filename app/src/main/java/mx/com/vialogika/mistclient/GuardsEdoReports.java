package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.vialogika.mistclient.Adapters.EdoGuardAdapter;
import mx.com.vialogika.mistclient.Pickers.DatePickerFragment;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.ApostamientoDetailsDialog;
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
    private List<GuardForceState> filter = new ArrayList<>();

    private RecyclerView               recyclerView;
    private RecyclerView.Adapter       rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int    siteid,groupRequired;
    private String from;
    private String to;
    private String providerfilter;
    private String groupFiltr;
    private String currentSite;

    private TextView dayDisplay,monthDisplay,groupNumber,reportedCount,requiredCount;
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
        displayDate(null);
        loadEdo();
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
        todaydates();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    private void todaydates(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        from = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        to = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    private void getItems() {
        recyclerView  = findViewById(R.id.edo_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter = new EdoGuardAdapter(edo);
        recyclerView.setAdapter(rvAdapter);
        dayDisplay      = findViewById(R.id.daydisplay     );
        monthDisplay    = findViewById(R.id.monthdisplay   );
        groupNumber = findViewById(R.id.groupnumbers);
        reportedCount = findViewById(R.id.reportedcount);
        requiredCount = findViewById(R.id.requiredcount);
        providerSpinner = findViewById(R.id.spinnerProvider);
        groupSpinner    = findViewById(R.id.spinnerGroup   );
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

    private void displayDate(@Nullable Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (date != null){
            c.setTime(date);
        }
        monthDisplay.setText(getMonthName(c.get(Calendar.MONTH)));
        dayDisplay.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
    }

    private void getGroupRequired(){
        dbo.getSiteGuardsRequired(siteid, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                groupRequired = (int) object;
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                updateGroupCounters();
            }
        });
    }

    private void updateGroupCounters(){
        reportedCount.setText(String.valueOf(edo.size()));
        requiredCount.setText(String.valueOf(groupRequired));
        groupNumber.setText(String.valueOf(grupos.size()));
    }

    private String getMonthName(int month){
        String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        return months[month];
    }

    private void getSortLists() {
        providers.clear();
        grupos.clear();
        String pinit = "";
        String ginit = "";
        for (int i = 0; i < filter.size(); i++) {
            GuardForceState item         = filter.get(i);
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
        updateGCounter();
        setFilterSpinnersIfEnabled();
        providerAdapter.notifyDataSetChanged();
        groupAdapter.notifyDataSetChanged();
    }

    private void updateGCounter(){
        groupNumber.setText(String.valueOf(grupos.size()));
    }

    private void setFilterSpinnersIfEnabled() {
        if (providers.size() <= 1) {
            providerSpinner.setEnabled(false);

        } else {
            providerSpinner.setEnabled(true);
        }
        if (grupos.size() <= 1){
            groupSpinner.setEnabled(false);
        }else{
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
               // Toast.makeText(GuardsEdoReports.this, "Site id : " + siteid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                providerfilter = providerSpinner.getSelectedItem().toString();
                filterResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupFiltr = groupSpinner.getSelectedItem().toString();
                filterResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        requiredCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAPDetailsDialog();
            }
        });
    }

    private void filterResults(){
        edo.clear();
        for (int i = 0; i < filter.size(); i++) {
            GuardForceState current = filter.get(i);
            int pid = getProviderIDByName(providerfilter);
            if (current.getProviderId() == pid && current.getEdoFuerzaTurno().equals(groupFiltr)){
                edo.add(current);
            }
            getGroupRequired();
            rvAdapter.notifyDataSetChanged();
        }
    }

    private Provider getProviderById(int id){
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getProviderId() == id){
                return proveedores.get(i);
            }
        }
        return null;
    }

    private int getProviderIDByName(String providerName){
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getProviderAlias().equals(providerName)){
                return proveedores.get(i).getProviderId();
            }
        }
        return 0;
    }

    private void loadEdo() {
        dbo.getEdoReports(siteid,from, to, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null) {
                    filter.clear();
                    edo.clear();
                    filter.addAll((List<GuardForceState>) object);
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                rvAdapter.notifyDataSetChanged();
                getSortLists();
                getGroupRequired();
            }
        });
    }

    private void showAPDetailsDialog(){
        ApostamientoDetailsDialog dialog = new ApostamientoDetailsDialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    private void syncEdo() {
        NetworkRequest.getEdoFuerza(this, from, to, siteid, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {

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
                displayDate(datefrom);
            }
        });
        datepicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateDates() {

    }
}
