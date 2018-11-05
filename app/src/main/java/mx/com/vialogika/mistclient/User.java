package mx.com.vialogika.mistclient;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    public int UID;

    public String APUUID = UUID.randomUUID().toString();
    public String firstName;
    public String middleName;
    public String lastname;
    public String userSite;

    public static void saveUserDatatoSP(Context context,JSONObject userData){
        try{
            SharedPreferences sp = context.getSharedPreferences("LogIn",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            String userFullName = userData.get("user_name").toString() + " " + userData.get("user_fname").toString() + " " + userData.get("user_lname").toString();
            editor.putString("user_login",userData.get("user_mail").toString());
            editor.putString("user_fullname",userFullName);
            editor.apply();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
