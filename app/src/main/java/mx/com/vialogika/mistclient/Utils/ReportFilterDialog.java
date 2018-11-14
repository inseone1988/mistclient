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
    private ArrayAdapter<String> mAdapter;

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
            mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,siteNames);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mAdapter);
        }else{
            siteNames.add("Loading items");
            setSpinnerEnabled();
            mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,siteNames);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void getDboInstance(){
        dbo = new DatabaseOperations(context);
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    mAdapter.clear();
                    sites = (ArrayList<Site>) object;
                    getSitenameList();
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show();
                    close();
                }
            }
        });
    }

    private void getSitenameList(){
        for (int i = 0;i < sites.size();i++){
            siteNames.add(sites.get(i).getSiteName());
        }
    }

    private void setSpinnerEnabled(){
        if(siteNames.size() == 1){
            mSpinner.setEnabled(false);
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
