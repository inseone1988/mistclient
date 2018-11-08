package mx.com.vialogika.mistclient.Utils;


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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import mx.com.vialogika.mistclient.Comment;

public class NetworkRequest {

    public static final String SERVER_URL_PREFIX = "https://www.vialogika.com.mx/dscic/";

    public static void authenticateUser(Context context , String user, String password,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String url = "https://www.vialogika.com.mx/dscic/raw.php";
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
            }
        });
        rq.add(jor);
    }

    public static void fetchIncidents(final Context context,int from,int user, int site,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String url = "https:www.vialogika.com.mx/dscic/raw.php";
        JSONObject params = new JSONObject();
        try{
            params.put("function","getIncidents")
                    .put("from",from)
                    .put("user",user)
                    .put("site",site);
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
                cb.onNetworkRequestError(error);
                NetworkResponse response = error.networkResponse;
                if (error instanceof NetworkError){
                    Dialogs.networkErrorDialog(context);
                }
                if (error instanceof TimeoutError){
                    Dialogs.timeoutError(context);
                }
                if(response != null){
                    Dialogs.invalidServerResponse(context,String.valueOf(response.statusCode));
                }
            }
        });
        rq.add(jor);
    }

    public static void saveComment(Context context,Comment comment,final NetworkRequestCallbacks cb){
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
                }
            });
            rq.add(jor);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void getEventComments(Context context,int eventid,final NetworkRequestCallbacks cb){
        String handler = "rawdata.php";
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
            }
        });
        rq.add(jor);
    }

    public static Bitmap getImageFromURL(String url){
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
