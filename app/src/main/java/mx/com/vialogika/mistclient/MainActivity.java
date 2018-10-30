package mx.com.vialogika.mistclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        setupToolbar();
    }

    private void setupToolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle(R.string.log_in_title);
        setSupportActionBar(mToolbar);
    }
}
