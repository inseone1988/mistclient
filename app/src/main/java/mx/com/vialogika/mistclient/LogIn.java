package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import mx.com.vialogika.mistclient.Utils.AuthCallbacks;
import mx.com.vialogika.mistclient.Utils.Authentication;
import mx.com.vialogika.mistclient.Utils.Dialogs;
import mx.com.vialogika.mistclient.Utils.SimpleDialogCallback;

public class LogIn extends AppCompatActivity {

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
                    getValues();
                    Authentication.authUser(v.getContext(), user, passwordString, new AuthCallbacks() {
                        @Override
                        public void onAuthenticated() {
                            clearFields();
                            setKeepSessionOpened(shouldSkipLogin);
                            startMainActivity();
                        }

                        @Override
                        public void onAuthenticatedFailed() {
                            clearFields();
                            Dialogs.authFailedDialog(mcontext);
                        }
                    });
                }
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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
