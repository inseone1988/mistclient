package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class RegisterProviderDialog extends MaterialDialog.Builder {

    private DatabaseOperations dbo;
    private Provider provider = new Provider();
    private String rs,alias,rfc;

    private EditText rsinput, aliasinput,rfcinput;

    public RegisterProviderDialog(Context context){
        super(context);
        init();
    }

    private void init(){
        title(R.string.new_provider);
        customView(R.layout.new_provider_dialog,true);
        positiveText(R.string.dsc_save);
        negativeText(R.string.cancel);
        cancelable = false;
        dbo = new DatabaseOperations(getContext());
        getItems();
        setupListeners();
    }

    private void getValues(){
        rs = rsinput.getText().toString();
        alias = aliasinput.getText().toString();
        rfc = rfcinput.getText().toString();
    }

    private void getItems(){
        rsinput = customView.findViewById(R.id.provider_rs);
        aliasinput = customView.findViewById(R.id.provider_alias);
        rfcinput = customView.findViewById(R.id.provider_rfc);
    }

    private boolean valuesEmpty(){
        String[] values = {rs,alias,rfc};
        for (int i = 0; i < values.length; i++){
            if (values[i].equals("")){
                return true;
            }
        }
        return false;
    }

    private boolean validInput(){
        getValues();
        if (valuesEmpty()){
            Toast.makeText(context, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupListeners(){
        onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                save(dialog);
            }
        });
    }

    private void save(final MaterialDialog dialog){
        if (validInput()){
            mapProvider();
            NetworkRequest.saveNewProvider(getContext(), provider, new NetworkRequestCallbacks() {
                @Override
                public void onNetworkRequestResponse(Object response) {
                    try{
                        final JSONObject resp = new JSONObject(response.toString());
                        if (resp.getBoolean("success")){
                            dbo.saveProvider(provider, new DatabaseOperations.doInBackgroundOperation() {
                                @Override
                                public void onOperationFinished(@Nullable Object object) {
                                    try{
                                        provider.setProviderId(resp.getInt("id"));
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new DatabaseOperations.UIThreadOperation() {
                                @Override
                                public void onOperationFinished(@Nullable Object object) {
                                    Toast.makeText(context, "Proveedor guardado correctamente", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }catch(JSONException e ){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNetworkRequestError(VolleyError error) {

                }
            });
        }
    }

    private void mapProvider(){
        provider.setProviderCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        provider.setProviderSocial(rs);
        provider.setProviderAlias(alias);
        provider.setProviderRfc(rfc);
        provider.setProviderClass("Seguridad Intramuros");
    }

}
