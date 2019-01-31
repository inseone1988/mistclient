package mx.com.vialogika.mistclient;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.mistclient.Adapters.EvidenceAdapter;
import mx.com.vialogika.mistclient.Pickers.DatePickerFragment;
import mx.com.vialogika.mistclient.Pickers.TimePickerFragment;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmergencyReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmergencyReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyReport extends Fragment {

    private Incident incident = new Incident();
    private EditText eventhour, ebentdate, facts,whhathappened;
    private TextView lastsaved;
    private Button cancel, save;
    private ImageButton triggerTimeSelect, triggerDateSelect,selectEvidence;
    private RecyclerView               rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter       adapter;
    private List<String>                   paths = new ArrayList<>();
    private List<Incident> pendingIncidents = new ArrayList<>();
    private ArrayList<String> pendingIncidentTitles = new ArrayList<>();
    private boolean continueEditing = true;
    private boolean autoSaveSchduled = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EmergencyReport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyReport.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyReport newInstance(String param1, String param2) {
        EmergencyReport fragment = new EmergencyReport();
        Bundle          args     = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_emergency_report, container, false);
        getItems(rootview);
        init();
        setListeners();
        setCurrentDateTime();
        getPendingIncidents();
        return rootview;
    }

    private void getItems(View rootview) {
        eventhour = rootview.findViewById(R.id.timeview);
        ebentdate = rootview.findViewById(R.id.timeview2);
        triggerTimeSelect = rootview.findViewById(R.id.timetrigger);
        triggerDateSelect = rootview.findViewById(R.id.timetrigger2);
        selectEvidence = rootview.findViewById(R.id.edorepbtn);
        whhathappened = rootview.findViewById(R.id.em_rep_what);
        facts = rootview.findViewById(R.id.em_rep_facts);
        cancel = rootview.findViewById(R.id.em_repbtn1);
        save = rootview.findViewById(R.id.em_repbtn2);
        rv = rootview.findViewById(R.id.em_rep_rv);
        lastsaved = rootview.findViewById(R.id.lastsavedtext);
    }

    private void init(){
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new EvidenceAdapter(paths);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    private void getPendingIncidents(){
        DatabaseOperations dbo = new DatabaseOperations(getContext());
        dbo.getPendingIncidents(new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    pendingIncidents.addAll((List<Incident>) object);
                    getPendingIncidentTitles();
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (pendingIncidents.size() > 0){
                    showPendingIncidentDialogs();
                }
            }
        });
    }

    private void showPendingIncidentDialogs(){
            new MaterialDialog.Builder(getContext())
                    .title("Reportes pendientes")
                    .items(pendingIncidentTitles)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            incident = pendingIncidents.get(position);
                            setValues();
                        }
                    })
                    .positiveText("Editar")
                    .show();
    }

    private void deleteIncident(){
        if (incident.getLocalId() != 0){
            DatabaseOperations dbo = new DatabaseOperations(getContext());
            dbo.deleteIncident(incident.getLocalId(), new DatabaseOperations.doInBackgroundOperation() {
                @Override
                public void onOperationFinished(@Nullable Object object) {
                    incident = new Incident();
                    getValues();
                }
            });
        }else{
            incident = new Incident();
            getValues();
        }
    }

    private void deleteDialog(){
        new MaterialDialog.Builder(getContext())
                .title("Eliminar reporte")
                .content("Desea borrar el reporte en edicion?")
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteIncident();
                    }
                })
                .negativeText("No seguir editando")
                .show();
    }

    private void getPendingIncidentTitles(){
        for (int i = 0; i < pendingIncidents.size(); i++) {
            String title = pendingIncidents.get(i).getEventWhat().equals("") ? "Reporte " + (i+1) : pendingIncidents.get(i).getEventWhat();
            pendingIncidentTitles.add(title);
        }
    }

    private void setListeners(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        triggerTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                ((TimePickerFragment) timepicker).setCb(new TimePickerFragment.TimePicker() {
                    @Override
                    public void onTimeSet(String time) {
                        updateTime(time);
                    }
                });
                timepicker.show(getFragmentManager(),"TIME_PICKER");
            }
        });
        triggerDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setCallback(new DatePickerFragment.DatePicker() {
                    @Override
                    public void onDateSet(Date from, Date to) {
                        updateDate(from);
                    }
                });
                datePicker.show(getFragmentManager(),"DATE_PICKER");
            }
        });
        selectEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEvidences();
            }
        });
        facts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 5){
                    if (!autoSaveSchduled){
                        setIncidentEditing();
                        scheduleAutoSave();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog();
            }
        });
    }

    private void saveDialog(){
        new MaterialDialog.Builder(getContext())
                .title("Enviar reporte")
                .content("Deseas enviar el reporte ahora?")
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getValues();
                        NetworkRequest.uploadIncidence(incident, getContext(), new NetworkRequest.NetworkUpload() {
                            @Override
                            public void onUploadCompleted(Incident incident, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Log.d("AndroidUploadService",String.valueOf(serverResponse.getHttpCode()));
                                deleteIncident();
                            }
                        });
                    }
                })
                .negativeText("Cancelar")
                .show();
    }

    private void getEvidences(){
        ImagePicker.create(this)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            List<Image> images = ImagePicker.getImages(data);
            for (int i = 0; i < images.size(); i++) {
                String path = images.get(i).getPath();
                paths.add(path);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setIncidentEditing(){
        incident.setEventEditStatus(true);
    }
    private void saveIncident(){
        getValues();
        Context context = getContext();
        if (context != null){
            DatabaseOperations dbo = new DatabaseOperations(getContext().getApplicationContext());
            dbo.saveIncident(incident, new DatabaseOperations.doInBackgroundOperation() {
                @Override
                public void onOperationFinished(@Nullable Object object) {

                }
            }, new DatabaseOperations.UIThreadOperation() {
                @Override
                public void onOperationFinished(@Nullable Object object) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
                    String formatted = String.format(getString(R.string.guardado),df.format(new Date()));
                    lastsaved.setText(formatted);
                }
            });
        }
    }

    private void scheduleAutoSave(){
        new CountDownTimer(900000,60000){
            @Override
            public void onTick(long millisUntilFinished) {
                saveIncident();
            }

            @Override
            public void onFinish() {
                autoSaveSchduled = false;
            }
        }.start();
        autoSaveSchduled = true;
    }

    private void getValues(){
        String cDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        incident.setEventRiskLevel("red");
        incident.setEventCaptureTimestamp(cDatetime);
        incident.setEventName("Reporte de emergencia");
        incident.setEventTime(eventhour.getText().toString());
        incident.setEventDate(ebentdate.getText().toString());
        incident.setEventWhat(whhathappened.getText().toString());
        incident.setEventFacts(facts.getText().toString());
        incident.setEventEvidence(TextUtils.join(",",paths));
    }

    private void setValues(){
        eventhour.setText(incident.getEventTime());
        ebentdate.setText(incident.getEventDate());
        whhathappened.setText(incident.getEventWhat());
        facts.setText(incident.getEventFacts());
        loadThumbnails();
    }

    private void loadThumbnails(){
        String mPaths = incident.getEventEvidence();
        String[] lPaths = mPaths.split(",");
        paths.addAll(new ArrayList<String>(Arrays.asList(lPaths)));
        adapter.notifyDataSetChanged();
    }

    private void updateTime(String time){
        eventhour.setText(time);
    }

    private void updateDate(Date from){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        ebentdate.setText(df.format(from));
    }

    private void setCurrentDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
        eventhour.setText(tf.format(new Date()));
        ebentdate.setText(df.format(new Date()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
