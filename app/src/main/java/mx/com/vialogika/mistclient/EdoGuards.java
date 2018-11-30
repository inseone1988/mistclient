package mx.com.vialogika.mistclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Adapters.GuardAdapter;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.PagerCallback;
import mx.com.vialogika.mistclient.Utils.Provider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdoGuards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdoGuards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdoGuards extends Fragment{

    private List<Guard> guards = new ArrayList<>();
    private List<Provider> providers;
    private List<Site> sites = new ArrayList<>();
    private Spinner siteSelect;
    private DatabaseOperations dbo;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EdoGuards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdoGuards.
     */
    // TODO: Rename and change types and number of parameters
    public static EdoGuards newInstance(String param1, String param2) {
        EdoGuards fragment = new EdoGuards();
        Bundle args = new Bundle();
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
        dbo = new DatabaseOperations(getContext());
        loadSites();
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        siteSelect = (Spinner) item.getActionView();
        setSpinnerOnSiteSelected();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSpinnerOnSiteSelected() {
        siteSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = siteSelect.getSelectedItem().toString();
                loadGuards(selected);
                Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_edo_guards, container, false);
        getItems(rootview);
        init();
        return rootview;
    }

    private void getItems(View rootview){
        mRecyclerView = rootview.findViewById(R.id.edo_guards_rv);
        mRecyclerView.setHasFixedSize(true);
    }

    private void init(){
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GuardAdapter(guards);
        mRecyclerView.setAdapter(mAdapter);
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

    private void loadSites(){
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                if (object != null){
                    sites.addAll((List<Site>) object);
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@android.support.annotation.Nullable Object object) {

            }
        });
    }

    private void loadGuards(String sitename){
        final Handler handler = new Handler(Looper.getMainLooper());
        clearAdapter();
        dbo.getGuardsFromSite(siteId(sitename), new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                if (response != null){
                    guards.addAll((List<Guard>) response);
                    Log.d("Guards",guards.size() + "guards loaded");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (guards.size() > 0){
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                dbo.close();
            }
        });

    }

    private void clearAdapter(){
        guards.clear();
        mAdapter.notifyDataSetChanged();
    }

    private int siteId(String site){
        for (int i = 0;i < sites.size(); i ++){
            Site cSite = sites.get(i);
            if (cSite.getSiteName().equals(site)){
                return cSite.getSiteId();
            }
        }
        return 0;
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
