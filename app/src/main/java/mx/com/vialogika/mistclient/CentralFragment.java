package mx.com.vialogika.mistclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import mx.com.vialogika.mistclient.Utils.NetworkRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CentralFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CentralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CentralFragment extends Fragment {

    private WebView webView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String  ARG_PARAM1 = "param1";
    private static final String  ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public CentralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CentralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CentralFragment newInstance(String param1, String param2) {
        CentralFragment fragment = new CentralFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_central, container, false);
        getItems(rootview);
        initWebView();
        return rootview;
    }

    private void getItems(View v){
        webView = v.findViewById(R.id.centralview);
    }

    private void initWebView(){
        String url = NetworkRequest.SERVER_URL_PREFIX + "android/apps/c/centralview.php";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
    }

    // TODO: Rename method, update argument and hook method into UI event
    //public void onButtonPressed(Uri uri) {
    //    if (mListener != null) {
    //        mListener.onFragmentInteraction(uri);
    //    }
    //}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
           // mListener = (OnFragmentInteractionListener) context;
        //} else {
         //   throw new RuntimeException(context.toString()
         //           + " must implement OnFragmentInteractionListener");
        //}
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
