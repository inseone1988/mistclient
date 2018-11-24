package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Apostamientos")
public class Apostamiento {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int remId;
    private int clId;
    private String apName;
    private String apAlias;
    private String apType;
    private int sId;

    public Apostamiento() {
    }

    public Apostamiento(JSONObject apostamiento){
        try{
            this.remId = apostamiento.getInt("plantilla_place_id");
            this.clId = apostamiento.getInt("plantilla_place_client_id");
            this.apName = apostamiento.getString("plantilla_place_apostamiento_name");
            this.apAlias = apostamiento.getString("plantilla_place_apostaamiento_alias");
            this.apType = apostamiento.getString("plantilla_place_type");
            this.sId = apostamiento.getInt("plantilla_place_site_id");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject mapData(){
        JSONObject ap = new JSONObject();
        try{
            ap.put("plantilla_place_id",this.remId);
            ap.put("plantilla_place_client_id",this.clId);
            ap.put("plantilla_place_apostamiento_name",this.apName);
            ap.put("plantilla_place_apostamiento_alias",this.apAlias);
            ap.put("plantilla_place_type",this.apType);
            ap.put("plantilla_place_site_id",this.sId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return ap;
    }

    public int getLocalId() {
        return localId;
    }

    public int getRemId() {
        return remId;
    }

    public void setRemId(int remId) {
        this.remId = remId;
    }

    public int getClId() {
        return clId;
    }

    public void setClId(int clId) {
        this.clId = clId;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public String getApAlias() {
        return apAlias;
    }

    public void setApAlias(String apAlias) {
        this.apAlias = apAlias;
    }

    public String getApType() {
        return apType;
    }

    public void setApType(String apType) {
        this.apType = apType;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }
}
