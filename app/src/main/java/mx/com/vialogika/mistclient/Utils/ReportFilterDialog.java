package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;

public class ReportFilterDialog extends MaterialDialog.Builder {

    private Spinner mSpinner;
    private DatabaseOperations dbo;
    private ArrayList<Site> sites;
    private List<String> siteNames = new ArrayList<>();
    final private ArrayAdapter<String> mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,siteNames);;

    public ReportFilterDialog(Context context){
        super(context);
        this.customView(R.layout.reports_filter_layout,true);
        this.title(R.string.filter_reports);
        this.positiveText(R.string.dsc_text_ok);
        this.negativeText(R.string.cancel);
        getItems();
        setupSpinner();
        setListeners();
    }

    private void getItems(){
        mSpinner = customView.findViewById(R.id.site_select);
        getDboInstance();
    }

    private void setupSpinner(){
        if (siteNames.size() > 1){
            setSpinnerEnabled();
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else{
            siteNames.add("Loading items...");
            setSpinnerEnabled();
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void getDboInstance(){
        dbo = new DatabaseOperations(context);
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                mapUserSitesToSpinner(object);
            }
        },null);
    }

    private void mapUserSitesToSpinner(Object object){
        if (object != null){
            mAdapter.clear();
            sites = (ArrayList<Site>) object;
            if (sites.size() > 0){
                getSitenameList();
            }else{
              siteNames.add("No sites found");
            }
            mAdapter.notifyDataSetChanged();
            setSpinnerEnabled();
        }else{
            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show();
            close();
        }
    }

    private void getSitenameList(){
        for (int i = 0;i < sites.size();i++){
            siteNames.add(sites.get(i).getSiteName());
        }
    }

    private void setSpinnerEnabled(){
        if(siteNames.size() == 1){
            mSpinner.setEnabled(false);
        }else{
            mSpinner.setEnabled(true);
        }
    }

    private void setListeners(){
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, String.valueOf(id), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void close(){
        this.close();
    }
}
