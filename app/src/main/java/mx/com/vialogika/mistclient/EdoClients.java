package mx.com.vialogika.mistclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Adapters.ClientAdapter;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.EditElementDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdoClients.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdoClients#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdoClients extends Fragment {

    private List<Client> clients = new ArrayList<>();
    private Spinner siteSelect;
    private DatabaseOperations dbo;

    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;

    private List<Site> sites = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EdoClients() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdoClients.
     */
    // TODO: Rename and change types and number of parameters
    public static EdoClients newInstance(String param1, String param2) {
        EdoClients fragment = new EdoClients();
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

    private void loadClients(String site){
        clearAdapter();
        dbo.getClientsBySiteId(siteId(site), new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                clients.addAll((List<Client>)object);
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                updateAdapter();
            }
        });
    }

    private void clearAdapter(){
        clients.clear();
        adapter.notifyDataSetChanged();
    }

    private void updateAdapter(){
        if (clients.size() > 0){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_edo_groups, container, false);
        // Inflate the layout for this fragment
        getItems(rootview);
        init();
        setListeners();
        return rootview;
    }

    private void getItems(View rootview){
        rv = rootview.findViewById(R.id.clients_rv);
        rv.setHasFixedSize(true);
       fab = rootview.findViewById(R.id.addClientfab);
    }

    private void setListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditElementDialog dialog = new EditElementDialog(getContext(),EditElementDialog.EDIT_CLIENT);
                dialog.setCl(new Client());
                dialog.show();
            }
        });
    }

    private void init(){
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new ClientAdapter(clients);
        rv.setAdapter(adapter);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.site_select_spinner);
        siteSelect = (Spinner) item.getActionView();
        setSpinnerOnSiteSelected();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSpinnerOnSiteSelected(){
        siteSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = siteSelect.getSelectedItem().toString();
                loadClients(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void loadSites(){
        dbo.getSiteNames(new DatabaseOperations.simpleOperationCallback() {
            @Override
            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                if (object != null){
                    sites.addAll((List<Site>) object);
                    Log.d("Room","Loaded : " + sites.size() + "clientes");
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@android.support.annotation.Nullable Object object) {

            }
        });
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
