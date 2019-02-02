package mx.com.vialogika.mistclient.Utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;

import mx.com.vialogika.mistclient.R;

public class FaqsDialogFragment extends DialogFragment {

    private WebView webView;

    public FaqsDialogFragment(){

    }

    public static FaqsDialogFragment newInstance(){
        //TODO: Add parameters if needed
        FaqsDialogFragment faqsDialogFragment = new FaqsDialogFragment();
        return faqsDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.faqs_layout,container,false);
        getItems(rootview);
        initWebView();
        return rootview;
    }

    private void getItems(View rootview){
        webView = rootview.findViewById(R.id.faqswebview);
    }

    private void initWebView(){
        String url = NetworkRequest.SERVER_URL_PREFIX + "android/apps/c/faqs.html";
        webView.loadUrl(url);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: get views and init
    }
}
