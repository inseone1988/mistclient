package mx.com.vialogika.mistclient.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Adapters.CoveredPlacesDetails;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class ApostamientoDetailsDialog extends DialogFragment {

    private RecyclerView               recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter       mAdapter;
    private TextView                   dialogPercent;
    private Button                     okbtn;

    private List<APDetailsDataHolder> placeDetails = new ArrayList<>();

    private String group,from,to;
    private int siteid,covered,mRequired,percentCovered;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getArguments().getString("group");
        siteid = getArguments().getInt("siteid");
        from = getArguments().getString("from");
        to = getArguments().getString("to");
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ap_details_dialog_layout, container, false);
        getItems(rootview);
        init();
        setListeners();
        return rootview;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Helllo");
        return dialog;
    }

    private void init(){
        mAdapter = new CoveredPlacesDetails(placeDetails);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
    }

    public static ApostamientoDetailsDialog newInstance(String from,String to,String group,int siteid){
        ApostamientoDetailsDialog d = new ApostamientoDetailsDialog();
        Bundle args = new Bundle();
        args.putString("from",from);
        args.putString("to",to);
        args.putString("group",group);
        args.putInt("siteid",siteid);
        d.setArguments(args);
        return d;
    }

    private void getData(){
        DatabaseOperations dbo = new DatabaseOperations(getContext());
        dbo.getAPCoveredDetails(from,to,group, siteid, new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                placeDetails.addAll((List<APDetailsDataHolder>) object);
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                mAdapter.notifyDataSetChanged();
                sumRequired();
            }
        });
    }

    private void getItems(View rootview) {
        recyclerView = rootview.findViewById(R.id.detailsview);
        dialogPercent = rootview.findViewById(R.id.dialogtotalpercent);
        okbtn = rootview.findViewById(R.id.okbtn);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(700,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    private void setListeners(){
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void sumRequired(){
        int total = 0;
        int required = 0;
        for (int i = 0; i < placeDetails.size(); i++) {
            APDetailsDataHolder d = placeDetails.get(i);
            total += d.getGuardsCovered();
            required += d.getGuardsRequired();
        }
        covered = total;
        mRequired = required;
        percentCovered = (covered * 100) / mRequired;
        String percent = String.valueOf(percentCovered) + " %";
        dialogPercent.setText(percent);
    }

}
