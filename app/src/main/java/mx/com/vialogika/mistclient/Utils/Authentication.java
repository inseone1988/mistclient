package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.mistclient.User;
import mx.com.vialogika.mistclient.UserSettings;

public class Authentication {

    public static void authUser(final Context context,String username,String password,final AuthCallbacks cb){
        final UserSettings ustt = new UserSettings(context);
        NetworkRequest.authenticateUser(context, username, password, new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                try{
                    JSONObject r = new JSONObject(response.toString());
                    if(r.has("success")){

                        boolean valid = r.getBoolean("success");
                        JSONObject userdata = r.getJSONArray("userdata").getJSONObject(0);
                        JSONArray usersites = userdata.getJSONArray("manages_sites");
                        JSONObject androidsettings = userdata.getJSONObject("android_settings");
                        if(valid){
                            User.saveUserDatatoSP(context, userdata, new User.onUserDataSaved() {
                                @Override
                                public void dataSaved() {
                                    cb.onAuthenticated();
                                }
                            });
                            User.saveUserSites(context,usersites);
                            ustt.mapAndroidSettings(androidsettings);
                            ustt.save();
                        }else{
                            cb.onAuthenticatedFailed();
                        }
                    }else{
                        cb.onAuthenticatedFailed();
                    }
                }catch(JSONException e){
                    Dialogs.malformedJSONDialog(context,e.getMessage());
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
