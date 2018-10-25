package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import mx.com.vialogika.mistclient.Utils.Dialogs;
import mx.com.vialogika.mistclient.Utils.SimpleDialogCallback;

public class LogIn extends AppCompatActivity {

    private Boolean shouldSkipLogin;

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if(!shouldSkipLogin){
            setContentView(R.layout.activity_log_in);
            getItems();
            setListeners();
        }else{
            //TODO:Here goes code to start main dashboard without prompt for password
        }
    }

    private void init(){
        shouldSkipLogin = getKeepSessionOpened();
    }

    private void getItems(){
        checkBox = findViewById(R.id.saveUser);
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
}
