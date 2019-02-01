package mx.com.vialogika.mistclient.Utils;


import android.app.DownloadManager;
import android.arch.persistence.room.Database;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.GuardForceState;
import mx.com.vialogika.mistclient.Incident;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class NetworkRequest {

    public static final String SERVER_URL_PREFIX = "https://www.vialogika.com.mx/dscic/";
    public static final String SERVER_URL_PHOTO_PREFIX = "https://www.vialogika.com.mx/";

    public static void authenticateUser(final Context context , String user, String password,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            params.put("function","authUser")
                    .put("user",user)
                    .put("password",password);
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                cb.onNetworkRequestResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cb.onNetworkRequestError(error);
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void fetchIncidents(final Context context,String mode,int from,int user, String[] sites,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            if (mode.equals("FETCH")){
                params.put("function","getIncidents")
                        .put("from",from)
                        .put("user",user)
                        .put("site",TextUtils.join(",",sites));
            }else{
                DatabaseOperations dbo = new DatabaseOperations(context);
                int[] toUpdate = dbo.repToUpdate();
                JSONArray indexes = new JSONArray(Arrays.asList(toUpdate));
                params.put("function","updateIncidents")
                        .put("reports",indexes);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cb.onNetworkRequestResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Make a new class for all network calls
                //25-12-2018 Added a generic dialog for all volley error
                cb.onNetworkRequestError(error);

            }
        });
        rq.add(jor);
    }

    public static void saveComment(final Context context,Comment comment,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = NetworkRequest.SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            Gson gson = new Gson();
            JSONObject desComment = new JSONObject(gson.toJson(comment));
            params.put("function","saveComment");
            params.put("data",desComment);
            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cb.onNetworkRequestResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void getEventComments(final Context context,int eventid,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = NetworkRequest.SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        RequestQueue rq = Volley.newRequestQueue(context);
        try{
            params.put("function","getEventComemnts");
            params.put("eventid",eventid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cb.onNetworkRequestResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cb.onNetworkRequestError(error);
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void flagReport(final Context context,int eventid,String flag,int userid,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        RequestQueue rq = Volley.newRequestQueue(context);
        try{
            params.put("function","flagReport");
            params.put("eventid",eventid);
            params.put("userid","userid");
            params.put("flag",flag);
        }catch(JSONException e ){
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
                public void onResponse(JSONObject response) {

                cb.onNetworkRequestResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cb.onNetworkRequestError(error);
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void getUserAutocomplete(final Context context,String term,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            params.put("function","userAutocomplete");
            params.put("term",term);
            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cb.onNetworkRequestResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void shareReport(final Context context,int userid,int reportid, String users,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX +handler;
        JSONObject params = new JSONObject();
        try{params.put("function","shareReport");
            params.put("userstoshare",users);
            params.put("reportid",reportid);
            params.put("userid",userid);
            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cb.onNetworkRequestResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void getSiteEdoInfo(final Context context, @Nullable String from, @Nullable String to, String sites, final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        if (from == null){
            from = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
        }
        if (to == null){
            to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        try{
            params.put("function","getEdoData");
            params.put("from",from);
            params.put("to",to);
            params.put("siteid",sites);
            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {
                    final DatabaseOperations dbo = new DatabaseOperations(context);
                    new DownloadProfileImagesDialog(context, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            switch (which){
                                case POSITIVE:
                                    dbo.syncEdoData(response,true);
                                    cb.onNetworkRequestResponse(response);
                                    break;
                                case NEGATIVE:
                                    dbo.syncEdoData(response,false);
                                    cb.onNetworkRequestResponse(response);
                                    break;
                            }
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static  void getProviderNames(final Context context,String serachterm,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            params.put("function","getProviderName");
            params.put("searchterm",serachterm);
            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cb.onNetworkRequestResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public static void saveNewProvider(final Context context,Provider provider,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        RequestQueue rq = Volley.newRequestQueue(context);
        try{
            params.put("function","saveProvider");
            params.put("data",provider.mapData());
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cb.onNetworkRequestResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onNetworkRequestError(error);
                    displayErrorDialog(context,error);
                }
            });
            rq.add(jor);
        }catch(JSONException e ){
            e.printStackTrace();
        }
    }

    public static void getEdoFuerza(final Context context, String from, String to, int siteid, final DatabaseOperations.doInBackgroundOperation dib, final DatabaseOperations.UIThreadOperation uiop){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            params.put("function","getEdo");
            params.put("from",from);
            params.put("to",to);
            params.put("siteid",siteid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.getBoolean("success")){
                        JSONArray payload = response.getJSONArray("payload");
                        DatabaseOperations dbo = new DatabaseOperations(context);
                        List<GuardForceState> responsedata = new ArrayList<>();
                        for (int i = 0; i < payload.length(); i++) {
                            responsedata.add(new GuardForceState(new JSONObject(payload.get(i).toString())));
                        }
                        dbo.syncEdoReports(responsedata, new DatabaseOperations.doInBackgroundOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                dib.onOperationFinished(object);
                            }
                        }, new DatabaseOperations.UIThreadOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                uiop.onOperationFinished(object);
                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void displayErrorDialog(Context context,VolleyError error){
        new NetworkErrorDialog(context,error);
    }

    public static void saveApostamiento(final Context context, final Apostamiento apostamiento, final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        RequestQueue rq = Volley.newRequestQueue(context);
        final DatabaseOperations dbo = new DatabaseOperations(context);
        JSONObject params = new JSONObject();
        try{
            params.put("function","saveApostamiento");
            params.put("data",apostamiento.mapData());
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.getBoolean("success")){

                        dbo.saveApostamiento(apostamiento.update(response.getJSONObject("payload")), new DatabaseOperations.doInBackgroundOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                cb.onNetworkRequestResponse(object);
                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void deleteApostamiento(final Context context,final int apid,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX +handler;
        JSONObject params = new JSONObject();
        final DatabaseOperations dbo = new DatabaseOperations(context);
        try{
            params.put("function","deleteapostamiento");
            params.put("apid",apid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.getBoolean("success")){
                        dbo.deleteApostamiento(apid, new DatabaseOperations.doInBackgroundOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {

                            }
                        }, new DatabaseOperations.UIThreadOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                cb.onNetworkRequestResponse(object);
                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void saveClient(final Context context,final Client client,final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        final DatabaseOperations dbo = new DatabaseOperations(context);
        try{
            params.put("function","saveClient");
            params.put("data",client.mapData());
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.getBoolean("success")){
                        dbo.saveClient(client.update(response.getJSONObject("payload")), new DatabaseOperations.doInBackgroundOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {

                            }
                        }, new DatabaseOperations.UIThreadOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                cb.onNetworkRequestResponse(object);
                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorDialog(context,error);
            }
        });
        rq.add(jor);
    }

    public static void deleteClient(final Context context, final int cid, final NetworkRequestCallbacks cb){
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        final DatabaseOperations dbo = new DatabaseOperations(context);
        try{
            //TODO:Add request params
            params.put("function","deleteClient");
            params.put("cid",cid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.getBoolean("success")){
                        dbo.deleteClient(cid, new DatabaseOperations.doInBackgroundOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {

                            }
                        }, new DatabaseOperations.UIThreadOperation() {
                            @Override
                            public void onOperationFinished(@android.support.annotation.Nullable Object object) {
                                cb.onNetworkRequestResponse(object);
                            }
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorDialog(context.getApplicationContext(),error);
            }
        });
        rq.add(jor);
    }

    public static ImageRequest getProfileImage(final Context context,String photoPath,final NetworkRequestCallbacks cb){
        String handler = "requesthandler.php";
        String url = SERVER_URL_PREFIX + handler;
        int count = 0;
        CustomImageRequest rq = new CustomImageRequest(url, photoPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (response !=null && !response.equals("")){
                    String path = saveImage(context,response);
                    cb.onNetworkRequestResponse(path);
                }
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cb.onNetworkRequestError(error);
                error.printStackTrace();
            }
        });
        return rq;
    }

    public static String saveImage(Context context,Bitmap mImage){
        String root = Environment.getExternalStorageDirectory().toString();
        File mFile = new File(root + "/Android/data/"+context.getPackageName()+ "/profileImages");
        if (!mFile.exists()){
            mFile.mkdirs();
        }
        Random generator = new Random();
        int    n         = 10000;
        n = generator.nextInt(n);
        String fname = "DSCProfile-"+ n +".jpg";
        File file = new File (mFile, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static void deleteGuardPictures(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dirpath = Environment.getExternalStorageDirectory().toString();
                File dir = new File(dirpath + "/Android/data/" + context.getPackageName() + "/profileImages");
                if (dir.isDirectory()){
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir,children[i]).delete();
                    }
                }
            }
        }).start();
    }

    public static void getImageFromURL(String url,boolean save,final NetworkBitmap cb){
        Bitmap image = null;
        final Handler handler = new Handler(Looper.getMainLooper());
        try{
            final URL link = new URL(url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        final Bitmap downloadStream =  BitmapFactory.decodeStream(link.openConnection().getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cb.downloaded(downloadStream);
                            }
                        });
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void uploadIncidence(final Incident incident, Context context,final NetworkUpload cb){
        try{
            String url = SERVER_URL_PREFIX + "requesthandler.php";
            String[] paths = incident.getEventEvidence().split(",");
            if (paths.length > 0){
               MultipartUploadRequest rq = new MultipartUploadRequest(context,url);
                for (int i = 0; i < paths.length; i++) {
                    if (!paths[i].equals("")){
                        rq.addFileToUpload(paths[i],"FILE-"+i);
                    }
                }
                rq.setDelegate(new UploadStatusDelegate() {
                    @Override
                    public void onProgress(Context context, UploadInfo uploadInfo) {

                    }

                    @Override
                    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                    }

                    @Override
                    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                        cb.onUploadCompleted(incident,uploadInfo,serverResponse);
                    }

                    @Override
                    public void onCancelled(Context context, UploadInfo uploadInfo) {

                    }
                });
                rq.addParameter("function","emergencyReport");
                rq.addParameter("data",incident.toJSON().toString());
                rq.setNotificationConfig(new UploadNotificationConfig());
                rq.setMaxRetries(2);
                rq.startUpload();
            }
        }catch(Exception e){
            Log.e("AndroidUploadService",e.getMessage(),e);
        }
    }

    public interface NetworkUpload{
        void onUploadCompleted(Incident incident, UploadInfo uploadInfo, ServerResponse serverResponse);
    }

    public interface NetworkBitmap{
        void downloaded(Bitmap bitmap);
    }
}
