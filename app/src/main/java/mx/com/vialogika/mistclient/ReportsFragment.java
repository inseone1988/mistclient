package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Utils.Color;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.Dialogs;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;
import mx.com.vialogika.mistclient.Utils.ReportFilterDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment {

    DatabaseOperations dbo;

    private Integer from = 1;
    private Context ctx = getContext();
    private int site;
    private int user;
    //TODO:Add Method to update the last id after network call update
    private int lastId;
    private List<Reporte> reportes = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private ConstraintLayout mConstarintLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportsFragment newInstance(String param1, String param2) {
        ReportsFragment fragment = new ReportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void init(){
        dbo = new DatabaseOperations(getContext().getApplicationContext());
        dbo.getAllReports(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                if(response != null){
                    reportes.addAll((List<Reporte>) response);
                }
            }
        });
        updateLastId();
        getUserData();
    }

    private void getUserData(){
        SharedPreferences sp = getActivity().getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        if (sp != null){
            user = sp.getInt("user_id",0);
            site = sp.getInt("user_site",0);
        }
    }

    private void updateLastId(){
        dbo.getLastReportId(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                lastId = (int) response;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        //Perform initializations an fragment event handlers;
        setHasOptionsMenu(true);
        getItems(root);
        setupRecyclerView();
        setupListeners();
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_filter:
                new ReportFilterDialog(this.getContext()).build().show();
                break;
            case R.id.archived_reports:
                dbo.getArchivedReports(new DatabaseOperationCallback() {
                    @Override
                    public void onOperationSucceded(@Nullable Object response) {
                        reportes.clear();
                        reportes.addAll((List<Reporte>) response);
                    }
                });
                if (reportes.size() > 0){
                    runLayoutAnimation();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.reports_summary_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupListeners(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO:Handle swipe event
                fetchIncidents(lastId);
            }
        });
    }

    private void hideRV(){
        mRecyclerview.setVisibility(View.GONE);
    }

    private void showRV(){
        mRecyclerview.setVisibility(View.VISIBLE);
    }

    private void hideNoDataLogo(){
        mConstarintLayout.setVisibility(View.GONE);
    }

    private void showNoDataLogo(){
        mConstarintLayout.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView(){
        mRecyclerview.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerview.setLayoutManager(rLayoutManager);
        rAdapter = new ReportAdapter(reportes, this.getContext(), new ReportActions() {
            @Override
            public void onReportArchived(int position) {
                removeItem(position);
            }

            @Override
            public void onReportShare(int position) {
               Dialogs.shareReportDialog(getContext(),user,position);
            }

            @Override
            public void onReportFlagged(int selected,int reportId) {
                String reportFlag = null;
                switch (selected){
                    case Reporte.REPORT_FLAG_ASIGNED:
                        reportFlag = "REPORT_ASIGNED";
                        break;
                    case Reporte.REPORT_FLAG_PENDING:
                        reportFlag = "REPORT_PENDING";
                        break;
                    case Reporte.REPORT_FLAG_RESOLVED:
                        reportFlag = "REPORT_RESOLVED";
                        break;
                }
                if (reportFlag != null){
                    NetworkRequest.flagReport(getContext(), reportId, reportFlag, user, new NetworkRequestCallbacks() {
                        @Override
                        public void onNetworkRequestResponse(Object response) {
                            try{
                                JSONObject rep = new JSONObject(response.toString());
                                if (rep.getBoolean("success")){
                                    Toast.makeText(getContext(), "Marcado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNetworkRequestError(VolleyError error) {

                        }
                    });
                }

            }
        });
        mRecyclerview.setAdapter(rAdapter);
        if (reportes.size() > 0){
            rAdapter.notifyDataSetChanged();
        }
    }

    private void removeItem(int position){
        reportes.remove(position);
        rAdapter.notifyItemRemoved(position);
        rAdapter.notifyItemRangeChanged(position,reportes.size());
    }

    private void getItems(View rootView){
        swipeRefreshLayout = rootView.findViewById(R.id.fr_report);
        mRecyclerview = rootView.findViewById(R.id.report_rv);
        mConstarintLayout = rootView.findViewById(R.id.nodataview);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        dbo.close();
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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

    private void addReportsToList(JSONArray response){
        for(int i = 0;i < response.length();i++){
            //reports must be JSONObject response object
            try{
                reportes.add(new Reporte(response.getJSONObject(i)));

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        runLayoutAnimation();
    }

    private void runLayoutAnimation(){
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this.getContext(),R.anim.layout_animation_fall_down);
        mRecyclerview.setLayoutAnimation(controller);
        mRecyclerview.getAdapter().notifyDataSetChanged();
        mRecyclerview.scheduleLayoutAnimation();
    }

    private void fetchIncidents(int from){
        dbo.syncReports(new DatabaseOperations.Sync() {
            @Override
            public void onReportSynced(List<Reporte> reports) {
                //This list contains reports fetched from network

            }

            @Override
            public void UIThreadOperation(Object response) {
                reportes.clear();
                //This list contains all reports
                if (response != null){
                    reportes.addAll((List<Reporte>)response);
                    runLayoutAnimation();
                    swipeRefreshLayout.setRefreshing(false);
                    if (reportes.size() > 0){
                        showRV();
                        hideNoDataLogo();
                    }else{
                        Toast.makeText(getContext(), "Timeline actualizado, no hay reportes", Toast.LENGTH_SHORT).show();
                        hideRV();
                        showNoDataLogo();
                    }
                }
            }
        });
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{

        final public static int REPORT_ACTION_MENU_GROUP = 0;
        final public static int REPORT_ACTION_ARCHIVE = 987654;
        final public static int REPORT_ACTION_SHARE = 321987;
        final public static int REPORT_ACTION_FLAG = 654321;

        private List<Reporte> mDataset;
        private Context context;
        private ReportActions actionCallbacks;

        public class ReportViewHolder extends RecyclerView.ViewHolder{

            CardView cardView;
            ImageView reportGrade,reportmenu;
            TextView reportTitle,reportTimeStamp,reportDescription,reportStatus;
            ReportViewHolder(View itemview){
                super(itemview);
                cardView = itemView.findViewById(R.id.report_cv);
                reportGrade = itemview.findViewById(R.id.report_grade);
                reportTitle = itemview.findViewById(R.id.report_title);
                reportTimeStamp = itemview.findViewById(R.id.report_timestamp);
                reportDescription = itemview.findViewById(R.id.report_exp);
                reportmenu = itemview.findViewById(R.id.r_menu);
                reportStatus = itemview.findViewById(R.id.report_status);
            }
        }

        public ReportAdapter(List<Reporte> reports,Context ctx,ReportActions actions){
            this.actionCallbacks = actions;
            this.mDataset = reports;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemview = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.report_card_view,viewGroup,false);
            //Perform view inits and listeners setup;

            return new ReportViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull final ReportViewHolder holder,final int i) {
            final Reporte report = mDataset.get(i);
            String grade = report.getReportGrade();
            int color = android.graphics.Color.parseColor(getResources().getString(Color.reportIconColor(grade)));
            //Loading default drawable changes color of all dataset, so we make a new drawable every time
            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_danger_sing_48);
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            setReportStatusDisplay(holder.reportStatus,report.getReportFlag());
            holder.reportGrade.setImageDrawable(drawable);
            holder.reportTitle.setText(report.getReportTitle());
            holder.reportTimeStamp.setText(report.getReportTimeStamp());
            holder.reportDescription.setText(report.getReportExplanation());
            holder.reportDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ReportView.class);
                    intent.putExtra("Reporte",report);
                    startActivity(intent);
                }
            });
            holder.reportmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(context,holder.reportmenu);
                    Menu mMenu = menu.getMenu();
                    setupMenu(mMenu);
                    menu.inflate(R.menu.report_menu);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case REPORT_ACTION_ARCHIVE:
                                    DatabaseOperations dbo = new DatabaseOperations(context);
                                    if(!report.getReportStatus().equals("Archived")){
                                        dbo.archiveReport(report.getRemReportId());
                                        Toast.makeText(context,"Reporte archivado",Toast.LENGTH_SHORT).show();
                                    }else{
                                        dbo.activeReport(report.getRemReportId());
                                        Toast.makeText(context, "Reporte ", Toast.LENGTH_SHORT).show();
                                    }
                                    actionCallbacks.onReportArchived(i);
                                    break;
                                case REPORT_ACTION_SHARE:
                                    actionCallbacks.onReportShare(report.getRemReportId());
                                    Toast.makeText(context, "Share report", Toast.LENGTH_SHORT).show();
                                    break;
                                case REPORT_ACTION_FLAG:
                                    Dialogs.reportflagDialog(context, new Dialogs.GenericDialogCallback() {
                                        @Override
                                        public void onActionDone(@android.support.annotation.Nullable Object params) {
                                            int selected = (int) params;
                                            actionCallbacks.onReportFlagged(selected,report.getRemReportId());
                                        }
                                    });
                                    break;
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            });
        }

        private void setReportStatusDisplay(TextView tv, String reportFlag){
            switch(reportFlag){
                case "REPORT_PENDING":
                    tv.setText(R.string.report_status_flag_pending);
                    break;
                case "REPORT_ASIGNED":
                    tv.setText(R.string.report_status_flag_asigned);
                    break;
                case "REPORT_RESOLVED":
                    tv.setText(R.string.report_status_flag_resolved);
                    break;
                    default:
                        tv.setText(R.string.report_status_flag_default);

            }
        }

        private void setupMenu(Menu menu){
            menu.clear();
            UserSettings us = new UserSettings(getContext());
            if (us.isCanArchiveReports()){
                menu.add(REPORT_ACTION_MENU_GROUP,REPORT_ACTION_ARCHIVE, Menu.NONE,"Archivar / Des");
            }
            if (us.isCanShareReports()){
                menu.add(REPORT_ACTION_MENU_GROUP,REPORT_ACTION_SHARE, Menu.NONE,"Enviar");
            }
            if (us.isCanFlagReports()){
                menu.add(REPORT_ACTION_MENU_GROUP,REPORT_ACTION_FLAG,Menu.NONE,"Marcar");
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public interface ReportActions{
        void onReportArchived(int position);
        void onReportShare(int position);
        void onReportFlagged(int selected,int reportid);

    }
}
