package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.mistclient.User;

public class Authentication {

    public static void authUser(final Context context,String username,String password,final AuthCallbacks cb){
        NetworkRequest.authenticateUser(context, username, password, new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    JSONObject r = new JSONObject(response.toString());
                    if(r.has("success")){
                        boolean valid = r.getBoolean("success");
                        if(valid){
                            User.saveUserDatatoSP(context, r.getJSONArray("userdata").getJSONObject(0), new User.onUserDataSaved() {
                                @Override
                                public void dataSaved() {
                                    cb.onAuthenticated();
                                }
                            });
                        }else{
                            cb.onAuthenticatedFailed();
                        }
                    }else{
                        cb.onAuthenticatedFailed();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetworkRequestError(VolleyError error) {
                cb.onAuthenticatedFailed();
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
    }
}
