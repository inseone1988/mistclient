package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import mx.com.vialogika.mistclient.R;

public class ReportShareDialog extends MaterialDialog.Builder {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTOCOMPLETE_DELAY = 300;

    private int mReportId,mUserId;

    private View rootview;
    private MultiAutoCompleteTextView atv;
    private EditText editText;
    private AutoSuggestAdapter asa;
    private Handler handler;


    ReportShareDialog(Context context,int reportid,int userid){
        super(context);
        this.mReportId = reportid;
        this.mUserId = userid;
        getItems();
        init();
        setupListeners();
    }

    private void getItems(){
        this.title = "Compartir reporte";
        this.customView(R.layout.share_report,true);
        this.positiveText(R.string.dsc_text_ok);
        this.negativeText(R.string.cancel);
        atv = customView.findViewById(R.id.usermail_autocomplete);
        asa = new AutoSuggestAdapter(context,android.R.layout.simple_dropdown_item_1line);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE){
                    if (!TextUtils.isEmpty(atv.getText())){
                        getUserSugegstions(getSearchString(atv.getText().toString()));
                    }
                }
                return false;
            }
        });
    }

    private String getSearchString(String searchterm){
        String searchString = null;
        String[] strings = searchterm.split(",");
        if (strings.length == 1){
            searchString = searchterm;
        }else{
            int last = (strings.length - 1);
            searchString = strings[last];
        }
        return searchString.trim();
    }

    private void getUserSugegstions(String searchString){
        final List<String> stringList = new ArrayList<>();
        if (searchString.length() > (atv.getThreshold() -1)){
            NetworkRequest.getUserAutocomplete(context, searchString, new NetworkRequestCallbacks() {
                @Override
                public void onNetworkRequestResponse(Object response) {
                    try{
                        JSONObject resp = (JSONObject) response;
                        JSONArray payload = resp.getJSONArray("payload");
                        for (int i = 0; i< payload.length();i++){
                            JSONObject sug = payload.getJSONObject(i);
                            stringList.add(sug.getString("user_mail"));
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    asa.setData(stringList);
                    asa.notifyDataSetChanged();
                }

                @Override
                public void onNetworkRequestError(VolleyError error) {
                    Toast.makeText(context, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init(){
        atv.setThreshold(3);

    }

    private String getuserStrings(){
        String users = "";
        String[] values = atv.getText().toString().split(",");
        for (int i = 0;i < (values.length -1); i++){
            values[i].trim();
        }
        return TextUtils.join(",",values);
    }

    private void setupListeners(){
        atv.setAdapter(asa);
        atv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        this.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                NetworkRequest.shareReport(context, mUserId, mReportId, getuserStrings(), new NetworkRequestCallbacks() {
                    @Override
                    public void onNetworkRequestResponse(Object response) {
                        try{
                            JSONObject resp = (JSONObject) response;
                            if (resp.getBoolean("success")){
                                Toast.makeText(context, "Shared corerctly", Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){

                        }
                    }

                    @Override
                    public void onNetworkRequestError(VolleyError error) {

                    }
                });
            }
        });
    }
}
