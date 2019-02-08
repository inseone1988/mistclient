package mx.com.vialogika.mistclient;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Adapters.ApostamientoAdapter;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.EditElementDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdoApostamientos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdoApostamientos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdoApostamientos extends Fragment {


    private Spinner siteSelekct;
    private List<Apostamiento> apostamientosList = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private DatabaseOperations dbo;

    private Context mContext;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EdoApostamientos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdoApostamientos.
     */
    // TODO: Rename and change types and number of parameters
    public static EdoApostamientos newInstance(String param1, String param2) {
        EdoApostamientos fragment = new EdoApostamientos();
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
        siteSelekct = (Spinner) item.getActionView();
        setSpinnerOnSiteSelected();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSpinnerOnSiteSelected(){
        siteSelekct.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = siteSelekct.getSelectedItem().toString();
                        loadApostamientos(selected);
                        Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
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

    private void loadApostamientos(String site){
        clearAdapter();
        dbo.getApostamientos(siteId(site),new DatabaseOperations.doInBackgroundOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                if (object != null){
                    apostamientosList.addAll((List<Apostamiento>) object);
                    Log.d("Room","Loaded :" + apostamientosList.size() + " Apostamientos");
                }

            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object object) {
                reloadAdapter();
            }
        });
    }

    private void clearAdapter(){
        apostamientosList.clear();
        adapter.notifyDataSetChanged();
    }

    private void reloadAdapter(){
        if (apostamientosList.size() > 0){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootview = inflater.inflate(R.layout.fragment_edo_apostamientos, container, false);
        getItems(rootview);
        init();
        setListeners();
        return rootview;
    }

    private void getItems(View rootView){
        recyclerView = rootView.findViewById(R.id.apostrv);
        fab = rootView.findViewById(R.id.addApostamiento);
        recyclerView.setHasFixedSize(true);
    }

    private void init(){
        final FragmentManager fm = mGetFragmentManager();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ApostamientoAdapter(apostamientosList){
            @Override
            public FragmentManager getFragmentManager() {
                return fm;
            }

            @Override
            public void apDeleted(int position) {
                removeitem(position);
            }
        };
        ((ApostamientoAdapter) adapter).setFragmentManager(fm);
        recyclerView.setAdapter(adapter);
    }

    private void removeitem(int position){
        apostamientosList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position,apostamientosList.size());
    }

    private FragmentManager mGetFragmentManager(){
        return ((AppCompatActivity) mContext).getSupportFragmentManager();
    }

    private FragmentManager getThisFragmentmanager(){
        return this.getFragmentManager();
    }

    private void setListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditElementDialog dialog = new EditElementDialog(getContext(),EditElementDialog.EDIT_APOSTAMIENTO);
                dialog.setAp(new Apostamiento());
                dialog.setCb(new EditElementDialog.callbacks() {
                    @Override
                    public void onSaved(Apostamiento apostamiento) {
                        apostamientosList.add(apostamiento);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
    }

    public Context getContext(){
        return this.mContext;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
