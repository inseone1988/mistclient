package mx.com.vialogika.mistclient;

import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Utils.EdoPagerAdapter;

public class EdoSettings extends AppCompatActivity implements EdoGroups.OnFragmentInteractionListener,EdoGuards.OnFragmentInteractionListener, EdoApostamientos.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edo_settings);
        setupTabs();
        setActionBarTitle();
    }

    private void setupTabs(){
        EdoPagerAdapter edoPagerAdapter = new EdoPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.edo_view_pager);
        pager.setAdapter(edoPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edo_settings_menu,menu);
        setupOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setActionBarTitle(){
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Intramuros");
    }

    private void setupOptionsMenu(Menu menu){
        List<String> sites = new ArrayList<>();
        sites.add("Macrocentro");
        sites.add("Nave 6");
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,sites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
