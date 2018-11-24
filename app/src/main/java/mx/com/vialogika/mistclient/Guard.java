package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Guards")
public class Guard {
    @PrimaryKey(autoGenerate = true)
    private int localId;
    private String pId;
    private String created;
    private int providerId;
    private String gType;
    private String gPosition;
    private String gName;
    private String gFname;
    private String gLname;
    private int gId;
    private boolean gStatus;
    private String baja;

    public Guard() {
    }

    public Guard(JSONObject guard){
        try{
            this.pId = guard.getString("guard_hash");
            this.created = guard.getString("person_created");
            this.providerId = guard.getInt("person_providerid");
            this.gType = guard.getString("guard_range");
            this.gPosition = guard.getString("person_position");
            this.gName = guard.getString("person_name");
            this.gFname = guard.getString("person_fname");
            this.gLname = guard.getString("person_lname");
            this.gId = guard.getInt("guard_id");
            this.gStatus = guard.getBoolean("guard_status");
            this.baja = guard.getString("guard_baja_timestamp");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject mapData(){
        JSONObject guard = new JSONObject();
        try{

            guard.put("guard_hash",this.pId);
            guard.put("person_created",this.created);
            guard.put("person_providerid",this.providerId);
            guard.put("guard_range",this.gType);
            guard.put("person_position",this.gPosition);
            guard.put("person_name",this.gName);
            guard.put("person_fname",this.gFname);
            guard.put("person_lname",this.gLname);
            guard.put("guard_id",this.gId);
            guard.put("guard_status",this.gStatus);
            guard.put("guard_baja_timestamp",this.baja);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return guard;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public String getgPosition() {
        return gPosition;
    }

    public void setgPosition(String gPosition) {
        this.gPosition = gPosition;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgFname() {
        return gFname;
    }

    public void setgFname(String gFname) {
        this.gFname = gFname;
    }

    public String getgLname() {
        return gLname;
    }

    public void setgLname(String gLname) {
        this.gLname = gLname;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }

    public boolean isgStatus() {
        return gStatus;
    }

    public void setgStatus(boolean gStatus) {
        this.gStatus = gStatus;
    }

    public String getBaja() {
        return baja;
    }

    public void setBaja(String baja) {
        this.baja = baja;
    }
}
