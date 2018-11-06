package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import mx.com.vialogika.mistclient.Room.AppDatabase;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Utils.Color;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;


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
    private int site;
    private int user;
    //TODO:Add Method to update the last id after network call update
    private int lastId;
    private List<Reporte> reportes = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

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
        dbo.getLastReportId(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                lastId = (int) response;
            }
        });
        getUserData();
    }

    private void getUserData(){
        SharedPreferences sp = getActivity().getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        if (sp != null){
            user = sp.getInt("user_id",0);
            site = sp.getInt("user_site",0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        //Perform initializations an fragment event handlers;
        getItems(root);
        setupRecyclerView();
        setupListeners();
        return root;
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

    private void setupRecyclerView(){
        mRecyclerview.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerview.setLayoutManager(rLayoutManager);
        rAdapter = new ReportAdapter(reportes,this.getContext());
        mRecyclerview.setAdapter(rAdapter);
    }

    private void getItems(View rootView){
        swipeRefreshLayout = rootView.findViewById(R.id.fr_report);
        mRecyclerview = rootView.findViewById(R.id.report_rv);

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
        rAdapter.notifyDataSetChanged();
    }

    private void fetchIncidents(int from){
        final Context ctx = getContext();
        new loadIncidents(from,user,site).execute(this.getContext(), new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    JSONObject JSOresponse = new JSONObject(response.toString());
                    JSONArray reports = JSOresponse.getJSONArray("reports");
                    if (reports.length() > 0){
                        addReportsToList(reports);
                        dbo.addReportsToQueue(reportes);
                        dbo.saveReports();
                    }else{
                        Toast.makeText(ctx,"Timeline up to date",Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNetworkRequestError(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{

        private List<Reporte> mDataset;
        private Context context;

        public class ReportViewHolder extends RecyclerView.ViewHolder{

            CardView cardView;
            ImageView reportGrade,reportmenu;
            TextView reportTitle,reportTimeStamp,reportDescription;
            ReportViewHolder(View itemview){
                super(itemview);
                cardView = itemView.findViewById(R.id.report_cv);
                reportGrade = itemview.findViewById(R.id.report_grade);
                reportTitle = itemview.findViewById(R.id.report_title);
                reportTimeStamp = itemview.findViewById(R.id.report_timestamp);
                reportDescription = itemview.findViewById(R.id.report_exp);
                reportmenu = itemview.findViewById(R.id.r_menu);
            }
        }

        public ReportAdapter(List<Reporte> reports,Context ctx){
            mDataset = reports;
            context = ctx;
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
                    menu.inflate(R.menu.report_menu);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.r_menu_archive:
                                    Toast.makeText(context,"Report is archived",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private class loadIncidents extends AsyncTask<Object,Void,Void>{

        private int mFrom;
        private int mUser;
        private int mSite;

        public loadIncidents(int from,int user,int site){
            this.mFrom = from;
            this.mUser = user;
            this.mSite = site;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Object... context) {
            NetworkRequest.fetchIncidents((Context)context[0],mFrom,mUser,mSite,(NetworkRequestCallbacks) context[1]);
            return null;
        }
    }
}
