package mx.com.vialogika.mistclient.Utils;


import android.arch.persistence.room.Database;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.GuardForceState;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class NetworkRequest {

    public static final String SERVER_URL_PREFIX = "https://www.vialogika.com.mx/dscic/";

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

    public static void fetchIncidents(final Context context,String mode,int from,int user, int site,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String handler = "raw.php";
        String url = SERVER_URL_PREFIX + handler;
        JSONObject params = new JSONObject();
        try{
            if (mode.equals("FETCH")){
                params.put("function","getIncidents")
                        .put("from",from)
                        .put("user",user)
                        .put("site",site);
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
                displayErrorDialog(context,error);

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
                public void onResponse(JSONObject response) {
                    DatabaseOperations dbo = new DatabaseOperations(context);
                    dbo.syncEdoData(response);
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

    public static void saveApostamiento(final Context context,Apostamiento apostamiento,final NetworkRequestCallbacks cb){
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
                        dbo.saveApostamiento(new Apostamiento(response.getJSONObject("payload")), new DatabaseOperations.doInBackgroundOperation() {
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

    public static Bitmap getImageFromURL(String url,boolean save){
        Bitmap image = null;
        URL link;
        try{
            link = new URL(url);
            image =  BitmapFactory.decodeStream(link.openConnection().getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }
}
