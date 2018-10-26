package mx.com.vialogika.mistclient.Utils;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkRequest {
    public static void authenticateUser(Context context , String user, String password,final NetworkRequestCallbacks cb){
        //TODO: Consider server will change later
        String url = "https:www.vialogika.com.mx/dscic/raw.php";
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
}
