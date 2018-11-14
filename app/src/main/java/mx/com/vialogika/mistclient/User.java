package mx.com.vialogika.mistclient;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.Room.Site;

@Entity(tableName = "Users")
public class User {
    @NonNull
    @PrimaryKey
    public int UID;

    public String APUUID = UUID.randomUUID().toString();
    public String firstName;
    public String middleName;
    public String lastname;
    public String userSite;

    @Ignore
    private Context ctx;

    public static void saveUserDatatoSP(Context context,JSONObject userData,onUserDataSaved cb){
        try{
            SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            String userFullName = userData.get("user_name").toString() + " " + userData.get("user_fname").toString() + " " + userData.get("user_lname").toString();
            editor.putString("user_login",userData.get("user_mail").toString());
            editor.putInt("user_id",userData.getInt("user_id"));
            editor.putInt("user_site",userData.getInt("user_site_id"));
            editor.putString("user_fullname",userFullName);
            editor.apply();
            cb.dataSaved();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveUserSites(Context context,@NotNull JSONArray sites){
        ArrayList<Site> lSites = new ArrayList<>();
        DatabaseOperations dbo = new DatabaseOperations(context);
        for (int i = 0;i < sites.length();i++){
            try{
                JSONObject site = sites.getJSONObject(i);
                lSites.add(new Site(site));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        dbo.saveSites(lSites);
    }

    public static int userId(Context context){
        SharedPreferences sp = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        return sp.getInt("user_id",0);
    }
    public static String userName(Context context){
        SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        return sp.getString("user_login","not_valid_user");
    }

    public static int userSite(Context context){
        SharedPreferences sp = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        return sp.getInt("user_site",0);
    }

    public static boolean getUserIsLoggedIn(Context context){
        SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        return sp.getBoolean("user_is_logged_in",false);
    }

    public static void setUserIsLoggedIn(Context context,boolean isLoggedIn){
        SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("user_is_logged_in",isLoggedIn);
        editor.apply();
    }

    public interface onUserDataSaved{
        void dataSaved();
    }
}
