package mx.com.vialogika.mistclient;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.UUID;

@Entity(tableName = "userdata")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uid")
    public int UID;

    public String APUUID = UUID.randomUUID().toString();
    public String firstName;
    public String middleName;
    public String lastname;
    public String userSite;

}
