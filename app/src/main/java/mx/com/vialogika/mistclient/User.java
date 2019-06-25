package mx.com.vialogika.mistclient;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;


import android.support.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.Depuracion;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

@Entity(tableName = "Users")
public class User {
    @NonNull
    @PrimaryKey
    public int UID;

    public String APUUID = UUID.randomUUID().toString();
    public static String API_KEY = "ak";
    public static String DEVICE_ID = "did";
    public String firstName;
    public String middleName;
    public String lastname;
    public String userSite;

    @Ignore
    private Context ctx;

    public static void saveUserDatatoSP(Context context,JSONObject userData,onUserDataSaved cb){
        try{
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            //SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            String userFullName = userData.get("user_name").toString() + " " + userData.get("user_fname").toString() + " " + userData.get("user_lname").toString();
            editor.putString(SettingsActivity.USERNAME_KEY,userData.get("user_mail").toString());
            editor.putInt("user_id",userData.getInt("user_id"));
            editor.putInt("user_site",userData.getInt("user_site_id"));
            editor.putString("user_fullname",userFullName);
            editor.apply();
            cb.dataSaved();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveUserSites(Context context,final @NonNull JSONArray sites){
        final List<Site> lSites = new ArrayList<>();
        final DatabaseOperations dbo = new DatabaseOperations(context);
        dbo.emptySitesTable(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                for (int i = 0;i < sites.length();i++){
                    try{
                        lSites.add(new Site(sites.getJSONObject(i)));
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                dbo.saveSites(lSites);
            }
        });
    }

    public static int userId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        return sp.getInt("user_id",0);
    }
    public static String userName(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        return sp.getString(SettingsActivity.USERNAME_KEY,"not_valid_user");
    }

    public static int userSite(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        return sp.getInt("user_site",0);
    }

    public static boolean getUserIsLoggedIn(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        return sp.getBoolean("user_is_logged_in",false);
    }

    public static void setUserIsLoggedIn(Context context,boolean isLoggedIn){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("user_is_logged_in",isLoggedIn);
        editor.apply();
    }

    public static void clearUserSP(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

    public static String[] getManagedSites(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences("AndroidSettings",Context.MODE_PRIVATE);
        String sitesString = sp.getString("casd","");
        if (sitesString != null){
            return sitesString.split(",");
        }
        return new String[]{};
    }

    public static void saveDeviceIdentifier(String deviceIdentifier){
        SharedPreferences p8references = PreferenceManager.getDefaultSharedPreferences(Initializer.getAppContext());
        SharedPreferences.Editor editor = p8references.edit();
        editor.putString(DEVICE_ID,deviceIdentifier);
        editor.apply();
    }

    public static void saveDeviceAPIKey(String apiKey){
        SharedPreferences p8references = PreferenceManager.getDefaultSharedPreferences(Initializer.getAppContext());
        SharedPreferences.Editor editor = p8references.edit();
        editor.putString(API_KEY,apiKey);
        editor.apply();
    }

    public static String getAPIKEY(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Initializer.getAppContext());
        return preferences.getString(API_KEY,"");
    }

    public static String getDeviceId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Initializer.getAppContext());
        return preferences.getString(DEVICE_ID,"");
    }

    public interface onUserDataSaved{
        void dataSaved();
    }
}
