package mx.com.vialogika.mistclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.evernote.android.job.JobManager;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.mistclient.Notif.RegisterNotificationChannels;
import mx.com.vialogika.mistclient.Utils.AuthCallbacks;
import mx.com.vialogika.mistclient.Utils.Authentication;
import mx.com.vialogika.mistclient.Utils.Dialogs;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;
import mx.com.vialogika.mistclient.Utils.SimpleDialogCallback;

public class LogIn extends AppCompatActivity {

    final static private int REQUEST_PERMISSIONS = 6546;

    private Boolean shouldSkipLogin;
    private String user;
    private String passwordString;
    private Context mcontext = this;

    private CheckBox checkBox;
    private Button loginbutton;
    private EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create NOtification Channel on Android O +
        new RegisterNotificationChannels(this);
        init();
        if(!shouldSkipLogin){
            setContentView(R.layout.activity_log_in);
            getItems();
            setListeners();
        }else{
            //TODO:Here goes code to start main dashboard without prompt for user and password
            startMainActivity();
        }

    }

    private void init(){
        shouldSkipLogin = getKeepSessionOpened();
    }

    private void getItems(){
        checkBox = findViewById(R.id.saveUser);
        loginbutton = findViewById(R.id.log_in);
        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
    }

    private void setListeners(){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(isChecked){
                    Dialogs.saveAccountDialog(buttonView.getContext(), new SimpleDialogCallback() {
                        @Override
                        public void AccountStayOpen() {
                            shouldSkipLogin = isChecked;
                        }
                    });
                }
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    if (checkPermission()){
                        getValues();
                        Authentication.authUser(v.getContext(), user, passwordString, new AuthCallbacks() {
                            @Override
                            public void onAuthenticated() {
                                clearFields();
                                setKeepSessionOpened(shouldSkipLogin);
                                User.setUserIsLoggedIn(mcontext,true);
                                loadAppData(new Callback() {
                                    @Override
                                    public void onDataLoaded() {
                                        startMainActivity();
                                    }
                                });
                            }

                            @Override
                            public void onAuthenticatedFailed() {
                                clearFields();
                                Dialogs.authFailedDialog(mcontext);
                            }
                        });
                    }else{
                        requestPermissons();
                    }
                }
            }
        });
    }

    private void loadAppData(final Callback cb){
        UserSettings us = new UserSettings(this);
        NetworkRequest.getSiteEdoInfo(this, null, null, us.getManagesSites(), new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    JSONObject resp = new JSONObject(response.toString());
                    if (resp.getBoolean("success")){
                        cb.onDataLoaded();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNetworkRequestError(VolleyError error) {

            }
        });
    }

    private boolean validateFields(){
        EditText[] fields = {username,password};
        for (int i = 0;i < fields.length;i++){
            String value = fields[i].getText().toString();
            if(value.equals("")){
                fields[i].requestFocus();
                Toast.makeText(this,R.string.field_cannot_be_empty,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void clearFields(){
        EditText[] fields = {username,password};
        for (int i = 0;i < fields.length;i++){
            fields[i].setText("");
        }
    }

    private void getValues(){
        user = username.getText().toString();
        passwordString = password.getText().toString();
    }

    private void setKeepSessionOpened(boolean state){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.should_auto_open),state);
        editor.apply();
    }

    private boolean getKeepSessionOpened(){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        return sp.getBoolean(getString(R.string.should_auto_open),false);
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,DscMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean checkPermission(){
        int wsp = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int resp = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (wsp == PackageManager.PERMISSION_GRANTED && resp == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissons(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PERMISSIONS :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loginbutton.performClick();
                }
                break;
        }
    }

    public interface Callback{
        void onDataLoaded();
    }
}
