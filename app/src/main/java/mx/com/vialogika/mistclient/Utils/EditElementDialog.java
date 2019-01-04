package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;

public class EditElementDialog extends MaterialDialog.Builder {

    final public static String EDIT_CLIENT       = "Cliente";
    final public static String EDIT_APOSTAMIENTO = "Apostaminto";
    final private static int UPDATE_ALL = 0;
    final private static  int UPDATE_SITES = 1;
    final private static int UPDATE_CLIENTS = 2;
    final private static int UPDATE_AP_TYPE = 3;

    private String mode;

    private List<Apostamiento> apts       = new ArrayList<>();
    private List<Client>       cls        = new ArrayList<>();
    private List<Site>         sites      = new ArrayList<>();
    private List<CharSequence> siteList   = new ArrayList<>();
    private List<CharSequence> clientList = new ArrayList<>();
    private callbacks cb;
    private clientCallback clcb;

    private TextView apName, apKey, gRequired,clientName,clientKey,clientAlias;
    private Spinner apType, apSite, apClient;

    private ArrayAdapter<CharSequence> adapter,siteAdapter,clientAdapter;

    private DatabaseOperations dbo;

    private Apostamiento ap;
    private Client       cl;

    public EditElementDialog(Context context, String mode) {
        super(context);
        this.mode = mode;
        getDatabaseOperations();
        setTitle();
        this.customView(getLayout(), true);
        this.positiveText("Guardar");
        getItems();
        setupSpinners();
        loadData();
        initAp();
        setupListeners();
        setOnGuardar();
    }

    private void setTitle() {
        switch (mode) {
            case EDIT_CLIENT:
                this.title("Editar cliente");
                break;
            case EDIT_APOSTAMIENTO:
                this.title("Editar Apostamiento");
                break;
        }
    }

    private int getLayout() {
        switch (mode) {
            case EDIT_CLIENT:
                return R.layout.client_edit_view;
            case EDIT_APOSTAMIENTO:
                return R.layout.edit_apostamiento;
        }
        return 0;
    }

    private void getDatabaseOperations() {
        dbo = new DatabaseOperations(getContext());
    }

    private void setupListeners(){
        switch(mode){
            case EDIT_APOSTAMIENTO:
                apType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       ap.setPlantillaPlaceType(apType.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                apSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch(mode){
                            case EDIT_APOSTAMIENTO:
                                ap.setPlantillaPlaceSiteId(sites.get(position).getSiteId());
                                break;
                            case EDIT_CLIENT:

                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                apClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ap.setPlantillaPlaceClientId(String.valueOf(cls.get(position).getClientId()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case EDIT_CLIENT:
                apSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cl.setClientSiteId(sites.get(position).getSiteId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }
    }

    private void getValues(){
        switch(mode){
            case EDIT_APOSTAMIENTO:
                ap.setPlantillaPlaceApostamientoName(apName.getText().toString());
                ap.setPlantillaPlaceApostamientoAlias(apKey.getText().toString());
                ap.setPlantillaPlaceGuardsRequired(Integer.valueOf(gRequired.getText().toString()));
                break;
            case EDIT_CLIENT:
                cl.setClientSocial(clientName.getText().toString());
                cl.setClientName(clientKey.getText().toString());
                cl.setClientAlias(clientAlias.getText().toString());
                break;
        }

    }

    private void loadData() {
        switch(mode){
            case EDIT_APOSTAMIENTO:
                getApostamientos();
                getClients();
                getSites();
                break;
            case EDIT_CLIENT:
                getClients();
                getSites();
                break;
        }
    }

    private void getSites(){
        final Handler handler = new Handler(Looper.getMainLooper());
        dbo.getAllSites(new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                sites.addAll((List<Site>) object);
                switch(mode){
                    case EDIT_APOSTAMIENTO:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                populateLists();
                                updateAdapters(UPDATE_CLIENTS);
                                updateAdapters(UPDATE_SITES);
                            }
                        });
                        break;
                    case EDIT_CLIENT:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                populateLists();
                                updateAdapters(UPDATE_SITES);
                            }
                        });

                        break;
                }
            }
        });
    }

    private void getClients(){
        final Handler handler = new Handler(Looper.getMainLooper());
        dbo.getAllClients(new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                cls.addAll((List<Client>) object);
                switch(mode){
                    case EDIT_APOSTAMIENTO:

                        break;
                    case EDIT_CLIENT:

                        break;
                }

            }
        });
    }

    private void getApostamientos(){
        dbo.getAllApostamientos(new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                apts.addAll((List<Apostamiento>) object);
            }
        });
    }

    private boolean validateInputs(){
        switch(mode){
            case EDIT_APOSTAMIENTO:
                return true;
            case EDIT_CLIENT:
                return true;

        }
        return true;
    }

    private void getItems() {
        MaterialDialog dialog = this.build();
        View rootview = dialog.getCustomView();
        switch (mode) {
            case EDIT_APOSTAMIENTO:
                apType = rootview.findViewById(R.id.apType);
                apName = rootview.findViewById(R.id.apname);
                apKey = rootview.findViewById(R.id.apkey);
                gRequired = rootview.findViewById(R.id.guardsrequired);
                apSite = rootview.findViewById(R.id.site_select);
                apClient = rootview.findViewById(R.id.client_select);
                break;
            case EDIT_CLIENT:
                apSite = rootview.findViewById(R.id.siteSpinner);
                clientName = rootview.findViewById(R.id.socialinput);
                clientKey = rootview.findViewById(R.id.clientnameinput);
                clientAlias = rootview.findViewById(R.id.clientaliasinput);
                break;
        }
    }

    private void initAp() {
        if (ap == null) {
            ap = new Apostamiento();
        }
    }

    private void setvalues() {
        switch (mode) {
            case EDIT_APOSTAMIENTO:
                apName.setText(ap.getPlantillaPlaceApostamientoName());
                apKey.setText(ap.getPlantillaPlaceApostamientoAlias());
                gRequired.setText(String.valueOf(ap.getPlantillaPlaceGuardsRequired()));
                apType.setSelection(adapter.getPosition(ap.getPlantillaPlaceType()));
                apSite.setSelection(siteAdapter.getPosition(getApNameByID(ap.getPlantillaPlaceSiteId())));
                apClient.setSelection(clientAdapter.getPosition(ap.getClientName()));
                break;
            case EDIT_CLIENT:
                clientName.setText(cl.getClientSocial());
                clientKey.setText(cl.getClientName());
                clientAlias.setText(cl.getClientAlias());
                apSite.setSelection(siteAdapter.getPosition(getApNameByID(cl.getClientSiteId())));
                break;
        }
    }

    private String getApNameByID(int id){
        for (int i = 0; i < sites.size(); i++) {
            if (sites.get(i).getSiteId() == id){
                return sites.get(i).getSiteName();
            }
        }
        return "";
    }

    private void populateLists() {
        switch (mode) {
            case EDIT_APOSTAMIENTO:
                getClientlists();
                getApostamientosLists();
                setvalues();
            break;
            case EDIT_CLIENT:
                //TODO:Populate lists
                getClientlists();
                setvalues();
                break;
        }
    }

    private void updateAdapters(int which){
        switch(which){
            case UPDATE_ALL:
                adapter.notifyDataSetChanged();
                siteAdapter.notifyDataSetChanged();
                clientAdapter.notifyDataSetChanged();
                break;
            case UPDATE_SITES:
                siteAdapter.notifyDataSetChanged();
                break;
            case UPDATE_CLIENTS:
                clientAdapter.notifyDataSetChanged();
                break;
            case UPDATE_AP_TYPE:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void getApostamientosLists() {
        for (int i = 0; i < cls.size(); i++) {
            clientList.add(cls.get(i).getClientAlias());
        }
    }

    private void getClientlists(){
        for (int i = 0; i < sites.size(); i++) {
            siteList.add(sites.get(i).getSiteName());
        }
    }

    private void setOnGuardar(){
        switch(mode){
            case EDIT_APOSTAMIENTO:
                onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (validateInputs()){
                            getValues();
                            NetworkRequest.saveApostamiento(getContext(), ap, new NetworkRequestCallbacks() {
                                @Override
                                public void onNetworkRequestResponse(Object response) {
                                    cb.onSaved((Apostamiento)response);
                                }
                                @Override
                                public void onNetworkRequestError(VolleyError error) {

                                }
                            });
                        }
                    }
                });
                break;
            case EDIT_CLIENT:
                onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (validateInputs()){
                            getValues();
                            NetworkRequest.saveClient(getContext(), cl, new NetworkRequestCallbacks() {
                                @Override
                                public void onNetworkRequestResponse(Object response) {
                                    clcb.onSaved((Client) response);
                                }

                                @Override
                                public void onNetworkRequestError(VolleyError error) {

                                }
                            });
                        }
                    }
                });
                break;
        }
    }

    public void setCb(callbacks cb) {
        this.cb = cb;
    }

    private void setupSpinners() {
        switch (mode) {
            case EDIT_APOSTAMIENTO:
                adapter = ArrayAdapter.createFromResource(getContext(), R.array.apType, android.R.layout.simple_spinner_item);
                siteAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,siteList);
                clientAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,clientList);
                siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                apSite.setAdapter(siteAdapter);
                apClient.setAdapter(clientAdapter);
                apType.setAdapter(adapter);
                break;
            case EDIT_CLIENT:
                siteAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,siteList);
                siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                apSite.setAdapter(siteAdapter);
                break;
        }
    }

    public Apostamiento getAp() {
        return ap;
    }

    public void setAp(Apostamiento ap) {
        this.ap = ap;
    }

    public Client getCl() {
        return cl;
    }

    public void setCl(Client cl) {
        this.cl = cl;
    }

    public clientCallback getClcb() {
        return clcb;
    }

    public void setClcb(clientCallback clcb) {
        this.clcb = clcb;
    }

    public interface callbacks{
        void onSaved(Apostamiento apostamiento);
    }

    public interface clientCallback{
        void onSaved(Client client);
    }
}
