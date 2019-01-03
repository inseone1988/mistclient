package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.vialogika.mistclient.Room.DatabaseOperations;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdoFragment extends Fragment {

    private TextView edoReports,edoConfig;

    private boolean paseed;

    private DatabaseOperations dbo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public EdoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdoFragment newInstance(String param1, String param2) {
        EdoFragment fragment = new EdoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edo, container, false);
        getitems(rootView);
        setlisteners();
        dbo = new DatabaseOperations(getContext());
        return rootView;
    }

    private void getitems(View rootview){
        edoConfig = rootview.findViewById(R.id.edo_settings);
        edoReports = rootview.findViewById(R.id.edost);
    }

    private void setlisteners(){
        edoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEdoConfigAcdtivity();
            }
        });
        edoReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                dbo.checkDatabase(new DatabaseOperations.doInBackgroundOperation() {
                    @Override
                    public void onOperationFinished(@Nullable Object object) {
                        paseed = (boolean) object;
                    }
                }, new DatabaseOperations.UIThreadOperation() {
                    @Override
                    public void onOperationFinished(@Nullable Object object) {
                        if (paseed){
                            loadGuardReportsActivity();
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), "Se deben de dar de alta guardias, proveedores y apostamientos para poder usar esta funcion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void loadGuardReportsActivity(){
        Intent intent = new Intent(this.getActivity(),GuardsEdoReports.class);
        startActivity(intent);
    }

    private void loadEdoConfigAcdtivity(){
        Intent intent = new Intent(this.getActivity(),EdoSettings.class);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /**if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /**if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
