package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class ProviderSelectDialog extends MaterialDialog.Builder {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTOCOMPLETE_DELAY = 300;

    private AutoCompleteTextView atv;
    private AutoSuggestAdapter asa;
    private Handler handler;

    private List<Provider> provHolder = new ArrayList<>();
    private DatabaseOperations dbo;
    private Provider provSelected;

    private providerDialogCallbacks cb;

    public ProviderSelectDialog(Context context,providerDialogCallbacks mcb){
        super(context);
        this.cb = mcb;
        this.title = "Buscar proveedor";
        this.positiveText("OK");
        this.neutralText("Nuevo proveedor");
        this.customView(R.layout.provider_autocomplete,false);
        atv = customView.findViewById(R.id.provider_search_term);
        atv.setThreshold(3);
        asa = new AutoSuggestAdapter(context,android.R.layout.simple_dropdown_item_1line);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE){
                    if (!TextUtils.isEmpty(atv.getText())){
                        getProviderSuggestions(getSearchString());
                    }
                }
                return false;
            }
        });
        setupListeners();
        dbo = new DatabaseOperations(getContext());
    }

    private String getSearchString(){
        return atv.getText().toString();
    }

    private void getProviderSuggestions(String searchString){
        final List<String> stringList = new ArrayList<>();
        NetworkRequest.getProviderNames(context, searchString, new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    JSONObject resp = (JSONObject) response;
                    JSONArray payload = resp.getJSONArray("payload");
                    for (int i = 0; i< payload.length();i++){
                        JSONObject sug = payload.getJSONObject(i);
                        provHolder.add(new Provider(sug));
                        stringList.add(sug.getString("provider_alias"));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                asa.setData(stringList);
                asa.notifyDataSetChanged();
            }

            @Override
            public void onNetworkRequestError(VolleyError error) {

            }
        });
    }

    private void setupListeners(){
        atv.setAdapter(asa);
        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = atv.getText().toString();
                provSelected = getProvider(selected);
            }
        });
        atv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,AUTOCOMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                if (provSelected != null){
                    dbo.saveProvider(provSelected, new DatabaseOperations.doInBackgroundOperation() {
                        @Override
                        public void onOperationFinished(@Nullable Object object) {
                            long saved = (long) object;
                            if (saved != 0){
                                Toast.makeText(context, "Provider saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new DatabaseOperations.UIThreadOperation() {
                        @Override
                        public void onOperationFinished(@Nullable Object object) {
                            dbo.close();
                            cb.onProviderSaved(provSelected);
                            dialog.hide();
                        }
                    });
                }else{
                    Toast.makeText(context, "Must select a provider", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Provider getProvider(String name){
        for (int i = 0; i < provHolder.size();i++){
            Provider provider = provHolder.get(i);
            if (provider.getProviderAlias().equals(name)){
                return provider;
            }
        }
        return null;
    }

    public interface providerDialogCallbacks{
        void onProviderSaved(Provider provider);
    }
}
