package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.gson.Gson;


import android.support.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.vialogika.mistclient.Room.AppDatabase;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Utils.ChatBubble;
import mx.com.vialogika.mistclient.Utils.CryptoHash;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.LoadImages;
import mx.com.vialogika.mistclient.Utils.LoadImagesCallback;
import mx.com.vialogika.mistclient.Utils.LoadSignatures;
import mx.com.vialogika.mistclient.Utils.Messages;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

public class ReportView extends AppCompatActivity {

    private Reporte report;
    private List<Comment> comments = new ArrayList<>();
    private int color;
    private int userId;

    private TextView editorName,dateTime,exp,whatExp,howExp,whereExp,factsExp,noComments;
    private ImageView riskLevel;
    private LinearLayout evContainer;
    private CardView evCard;
    private CardView sigCard;
    private LinearLayout sigContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        getReport();
        setupActionBar();
        getItems();
        setValues();
        setListeners();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int shareid = 101;
        MenuItem share  = menu.add(Menu.NONE,shareid,Menu.NONE,"Comparttir");
        share.setIcon(R.drawable.ic_share_black_24dp);
        share.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new MaterialDialog.Builder(ReportView.this)
                        .title("Compartir reporte")
                        .content("¿Seguro que desea compartir el reporte?")
                        .positiveText("OK")
                        .negativeText("Cancelar")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                shareReport();
                            }
                        }).show();
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void shareReport(){
        Toast.makeText(this, "Obteniendo token de seguridad...", Toast.LENGTH_SHORT).show();
        NetworkRequest.getSecurityToken(this, Reporte.REPORT_TOKEN_REQUEST,report.getReportId(), new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                JSONObject mResponse = (JSONObject) response;
                if (response != null){
                    try{
                        if (mResponse.getBoolean("success")){
                            String token = mResponse.getString("token");
                            String reportId = CryptoHash.sha256(String.valueOf(report.getReportId()));
                            openIntentPicker(token,reportId);
                        }
                    }catch(JSONException e){
                        Toast.makeText(ReportView.this, "Failed to get security token. Try again later", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onNetworkRequestError(VolleyError error) {

            }
        });
    }

    public void openIntentPicker(String token,String reportId){
        UserSettings settings = new UserSettings(this);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //TODO:Define default mail get method
        intent.putExtra(Intent.EXTRA_SUBJECT,"Nueva incidencia reportada");
        //TODO: Crear metodo para obtener el token de seguridad del mail
        intent.putExtra(Intent.EXTRA_TEXT,"Hello this is an sample text check it at "+NetworkRequest.SERVER_URL_PREFIX+"rViewer.php?token=" + token +"&reportId=" + reportId);
        startActivity(intent);
    }

    private void getReport(){
        Intent intent  = getIntent();
        if(intent.hasExtra("Reporte")){
            report = (Reporte) intent.getSerializableExtra("Reporte");
            color = Color.parseColor(getResources().getString(mx.com.vialogika.mistclient.Utils.Color.reportIconColor(report.getReportGrade())));
        }else{
            finish();
        }
    }

    private void getItems(){
        userId = User.userId(this);
        riskLevel = findViewById(R.id.imageView3);
        editorName = findViewById(R.id.editor_name);
        dateTime = findViewById(R.id.date_time);
        exp = findViewById(R.id.exp);
        whatExp = findViewById(R.id.what_exp);
        howExp = findViewById(R.id.how_exp);
        whereExp = findViewById(R.id.where_exp);
        factsExp = findViewById(R.id.exp_facts);
        evCard = findViewById(R.id.evidences_card);
        evContainer = findViewById(R.id.evidences_container);
        sigCard = findViewById(R.id.signatures_card);
        sigContainer = findViewById(R.id.signatures_container);
        noComments = getNoComments();
    }

    private void loadComments(){
        final Context ctx = this;
        final DatabaseOperations dbo = new DatabaseOperations(ctx);
        NetworkRequest.getEventComments(ctx, report.getRemReportId(), new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    Gson gson = new Gson();
                    JSONObject resp = new JSONObject(response.toString());
                    JSONArray comms = resp.getJSONArray("comments");
                    if (comms.length() > 0){
                        for (int i = 0;i < comms.length();i++){
                            Comment com = gson.fromJson(comms.getJSONObject(i).toString(),Comment.class);
                            comments.add(com);
                        }
                        dbo.saveComments(comments,null);
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

    private int setCommentType(String username){
        return User.userName(this).equals(username) ? Messages.MESSAGE_OUTBOUND : Messages.MESSAGE_INBOUND;
    }

    private TextView getNoComments(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,25,0,0);
        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(R.string.be_the_first_to_comment);
        tv.setTextSize(16);
        tv.setTypeface(null, Typeface.ITALIC);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private void setValues(){
        riskLevel.setColorFilter(color);
        editorName.setText(report.getUsername());
        dateTime.setText(report.getReportTimeStamp());
        exp.setText(report.getReportTitle());
        whatExp.setText(report.getEventWhat());
        howExp.setText(report.getEventHow());
        whereExp.setText(report.getEventWhere());
        factsExp.setText(report.getReportExplanation());
        loadImages();
        loadComments();
        //TODO:Remove testing method
        //bubbleExampleSetup();
    }

    private void setListeners(){
    }

    private void setupActionBar(){
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setTitle(report.getReportTitle());
    }

    private void loadImages(){
        if(report.hasEvidences()){
            evCard.setVisibility(View.VISIBLE);
            loadNext(0);
        }
    }

    private void displaySignatures(){
        if (report.hasSignatures()){
            sigCard.setVisibility(View.VISIBLE);
            loadSignatures(0);
        }
    }

    private void loadNext(int mPosition){
        String[] evidences = report.getEvidences();
        //TODO: Consider server will change
        String prefix = "https://www.vialogika.com.mx";
        if(mPosition < evidences.length){
            String url = prefix + evidences[mPosition];
            new LoadImages(new LoadImagesCallback() {
                @Override
                public void onImagesLoaded(Bitmap image, int position) {
                    if (image != null){
                        ImageView holder = evidenceImageView(image);
                        holder.setOnTouchListener(new ImageMatrixTouchHandler(holder.getContext()));
                        evContainer.addView(holder);
                        loadNext(position);
                    }
                }
            },mPosition).execute(url);
        }else{
            displaySignatures();
        }
    }

    private void loadSignatures(final int Position){
        String[] signatures = report.getSignatures();
        final String[] sroles = report.getSignaturesRoles();
        final String[] signNames = report.getSigantureNames();
        //TODO:Consider server will change later
        String urlPrefix = "https://www.vialogika.com.mx";
        if(Position < signatures.length){
            String url = urlPrefix + signatures[Position];
            new LoadSignatures(new LoadImagesCallback() {
                @Override
                public void onImagesLoaded(Bitmap image, int position) {
                    if(image != null){
                        addSignature(Position,image,signNames[Position],sroles[Position]);
                        loadSignatures(position);
                    }
                }
            },Position).execute(url);
        }
    }

    private void addSignature(int position,Bitmap image,String signaturename,String srole){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.8f);
        LinearLayout.LayoutParams contParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.5f);
        LinearLayout ll;
        ImageView iv = evidenceImageView(image);
        //iv.setLayoutParams(params);
        int numChilds = (sigCard.getChildCount() - 1);
        if(numChilds < 1){
            ll = setupLLayout();
            sigContainer.addView(ll);
        }else{
            ll = (LinearLayout) sigCard.getChildAt(Math.round(position / 2));
            if (ll == null){
                ll = setupLLayout();
                sigContainer.addView(ll);
            }
        }
        //iv.setMaxWidth((ll.getWidth() / 2));
        ll.setLayoutParams(contParams);
        ll.addView(iv);
        ll.addView(signatureName(signaturename));
        ll.addView(srole(srole));
    }

    private LinearLayout setupLLayout(){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        return ll;
    }

    private TextView signatureName(String name){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.1f);
        TextView  mName = new TextView(this);
        mName.setGravity(View.TEXT_ALIGNMENT_CENTER);
        mName.setLayoutParams(layoutParams);
        mName.setText(name);
        return mName;
    }

    private TextView srole(String srole){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,0.1f);
        TextView sRole = new TextView(this);
        sRole.setGravity(View.TEXT_ALIGNMENT_CENTER);
        sRole.setLayoutParams(layoutParams);
        sRole.setText(srole);
        return sRole;
    }

    private ImageView evidenceImageView(Bitmap image){
        ImageView evidenceGraphic = new ImageView(this);
        evidenceGraphic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        evidenceGraphic.setAdjustViewBounds(true);
        evidenceGraphic.setPadding(0,10,0,10);
        evidenceGraphic.setImageBitmap(image);
        return evidenceGraphic;
    }
}
