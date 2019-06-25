package mx.com.vialogika.mistclient.Network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.com.vialogika.mistclient.Initializer;
import mx.com.vialogika.mistclient.User;
import mx.com.vialogika.mistclient.Utils.CryptoHash;

public class ServerRequest extends JsonObjectRequest {

    public ServerRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreferences      preferences = PreferenceManager.getDefaultSharedPreferences(Initializer.getAppContext());
        String                 authToken   = preferences.getString(User.API_KEY,"");
        String                 deviceId    = CryptoHash.sha256(preferences.getString(User.DEVICE_ID,"0"));
        HashMap<String,String> headers     = new HashMap<>();
        if (authToken.equals("")){
            headers.put("request","true");
            headers.put("identifier",deviceId);
        }else{
            headers.put("bearer",authToken);
        }
        return headers;
    }
}
