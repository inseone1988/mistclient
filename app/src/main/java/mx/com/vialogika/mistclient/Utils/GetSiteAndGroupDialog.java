package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;

public class GetSiteAndGroupDialog extends MaterialDialog.Builder {

    private Spinner site,prov,group;

    private List<CharSequence> sites = new ArrayList<>();
    private List<CharSequence> groups = new ArrayList<>();

    private List<Site> mSites = new ArrayList<>();
    private String[] mGroups;

    private ArrayAdapter<CharSequence> sAdapter;
    private ArrayAdapter<CharSequence> gAdapter;
    private SiteAndGroupsCallback mCb;
    private String grupo;
    private int siteid;

    public GetSiteAndGroupDialog(@NonNull Context context,SiteAndGroupsCallback cb) {
        super(context);
        mCb = cb;
        customView(R.layout.site_and_group_select_dialog,true);
        enableOOK();
        getItems();
        init();
        getData();
        setListeners();
        this.title = "Selecciona grupo";
    }

    private void init(){
        sAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,sites);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        site.setAdapter(sAdapter);
        gAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,groups);
        gAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(gAdapter);
    }

    private void getData(){
        String[] todaydates = todaydates();
        DatabaseOperations dbo = new DatabaseOperations(context);
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                mSites = (List<Site>) object;
                if (mSites != null){
                    for (int i = 0; i < mSites.size(); i++) {
                        sites.add(mSites.get(i).getSiteName());
                    }
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                sAdapter.notifyDataSetChanged();
            }
        });
        dbo.getGroupNames(todaydates[0], todaydates[1], new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                mGroups = (String[]) object;
                if (mGroups != null){
                    for (int i = 0; i < mGroups.length; i++) {
                        groups.add(mGroups[i]);
                    }
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                gAdapter.notifyDataSetChanged();
                if (mGroups != null){
                    if (mGroups.length == 0){
                        group.setEnabled(false);
                    }
                }

            }
        });
    }

    private void enableCancel(){
        this.negativeText = "Cancelar";
    }

    private void enableOOK(){
        this.positiveText("OK");
    }

    private int getSiteID(String sitename){
        for (int i = 0; i < mSites.size(); i++) {
            if (mSites.get(i).getSiteName().equals(sitename)){
                return mSites.get(i).getSiteId();
            }
        }
        return 0;
    }

    private void setListeners(){
        site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                siteid = getSiteID(site.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (mGroups.length != 0){
                    mCb.onValuesSelected(siteid,group.getSelectedItem().toString());
                }else{
                    Toast.makeText(context, "No se ha reportado ningun grupo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String[] todaydates(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        String from = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(new Date());
        String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        return new String[]{from,to};
    }

    private void getItems(){
        site = customView.findViewById(R.id.site_spinner);
        group = customView.findViewById(R.id.group_spinner);
    }

    public interface SiteAndGroupsCallback{
        void onValuesSelected(int site, String grupo);
    }
}
