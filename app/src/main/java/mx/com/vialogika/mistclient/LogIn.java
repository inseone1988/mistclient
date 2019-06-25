package mx.com.vialogika.mistclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
    private String  user;
    private String  passwordString;
    private Context mcontext = this;

    private CheckBox checkBox;
    private Button   loginbutton;
    private EditText username, password;

    private UserSettings us;
    private boolean      canContinue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create NOtification Channel on Android O +
        new RegisterNotificationChannels(this);
        init();
        if (!shouldSkipLogin) {
            setContentView(R.layout.activity_log_in);
            getItems();
            setListeners();
        } else {
            //TODO:Here goes code to start main dashboard without prompt for user and password
            startMainActivity();
        }

    }

    private void init() {
        shouldSkipLogin = getKeepSessionOpened();
    }

    private void getItems() {
        checkBox = findViewById(R.id.saveUser);
        loginbutton = findViewById(R.id.log_in);
        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
    }

    private String getImei() {
        String imei = User.getDeviceId();
        if (!imei.equals("")) {
            return imei;
        } else {
            int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imei = manager.getDeviceId();
                User.saveDeviceIdentifier(imei);
            }
        }
        return imei;
    }

    private void auth(){
        Authentication.authUser(this, user, passwordString, new AuthCallbacks() {
            @Override
            public void onAuthenticated() {
                clearFields();
                setKeepSessionOpened(shouldSkipLogin);
                User.setUserIsLoggedIn(mcontext, true);
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
    }

    private void setListeners() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
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
                getImei();
                if (validateFields()) {
                    if (checkPermission()) {
                        getValues();
                        if (!getApiKey().equals("")) {
                            auth();
                        }else{
                            NetworkRequest.getApiKey(new NetworkRequestCallbacks() {
                                @Override
                                public void onNetworkRequestResponse(Object response) {
                                    if (!getApiKey().equals("")){
                                        auth();
                                    }else {
                                        Toast.makeText(mcontext, "No se ha podido comprobar la identidad del dispositivo", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNetworkRequestError(VolleyError error) {

                                }
                            });
                        }
                    } else {
                        requestPermissons();
                    }
                }
            }
        });
    }

    private String getApiKey() {
        return User.getAPIKEY();
    }

    private void loadAppData(final Callback cb) {
        us = new UserSettings(this);
        String manageSites = us.getManagesSites();
        if (!manageSites.equals("undefined")) {
            NetworkRequest.getSiteEdoInfo(this, null, null, us.getManagesSites(), new NetworkRequestCallbacks() {
                @Override
                public void onNetworkRequestResponse(Object response) {
                    try {
                        JSONObject resp = new JSONObject(response.toString());
                        if (resp.getBoolean("success")) {
                            cb.onDataLoaded();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNetworkRequestError(VolleyError error) {

                }
            });
        } else {
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    us = new UserSettings(getContext());
                    if (!us.getManagesSites().equals("undefined")) {
                        canContinue = true;
                    }
                }

                @Override
                public void onFinish() {
                    if (canContinue) {
                        loadAppData(new Callback() {
                            @Override
                            public void onDataLoaded() {
                                startMainActivity();
                            }
                        });
                    }
                }
            };
        }

    }

    private Context getContext() {
        return this;
    }

    private boolean validateFields() {
        EditText[] fields = {username, password};
        for (int i = 0; i < fields.length; i++) {
            String value = fields[i].getText().toString();
            if (value.equals("")) {
                fields[i].requestFocus();
                Toast.makeText(this, R.string.field_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void clearFields() {
        EditText[] fields = {username, password};
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText("");
        }
    }

    private void getValues() {
        user = username.getText().toString();
        passwordString = password.getText().toString();
    }

    private void setKeepSessionOpened(boolean state) {
        SharedPreferences        sp     = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.should_auto_open), state);
        editor.apply();
    }

    private boolean getKeepSessionOpened() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        return sp.getBoolean(getString(R.string.should_auto_open), false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, DscMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean checkPermission() {
        int wsp                 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int resp                = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int phoneCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int phonestate          = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (wsp == PackageManager.PERMISSION_GRANTED && resp == PackageManager.PERMISSION_GRANTED && phoneCallPermission == PackageManager.PERMISSION_GRANTED && phonestate == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissons() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImei();
                    loginbutton.performClick();
                }
                break;
        }
    }

    public interface Callback {
        void onDataLoaded();
    }
}
