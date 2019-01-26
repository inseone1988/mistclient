package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Apostamientos")
public class Apostamiento {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int plantillaPlaceId;
    private String plantillaPlaceClientId;
    private String plantillaPlaceApostamientoName;
    private String plantillaPlaceApostamientoAlias;
    private String plantillaPlaceType;
    private int plantillaPlaceSiteId;
    private String clientName;
    private int plantillaPlaceGuardsRequired;
    private int plantillaPlaceStatus;
    private String plantillaPlaceConsExp;

    public Apostamiento() {
    }

    public Apostamiento(JSONObject apostamiento) {
        try {
            this.plantillaPlaceId = apostamiento.getInt("plantilla_place_id");
            this.plantillaPlaceClientId = apostamiento.getString("plantilla_place_client_id");
            this.plantillaPlaceApostamientoName = apostamiento.getString("plantilla_place_apostamiento_name");
            this.plantillaPlaceApostamientoAlias = apostamiento.getString("plantilla_place_apostamiento_alias");
            this.plantillaPlaceType = apostamiento.getString("plantilla_place_type");
            this.plantillaPlaceSiteId = apostamiento.getInt("plantilla_place_site_id");
            this.plantillaPlaceGuardsRequired = apostamiento.getInt("plantilla_place_guards_required");
            this.plantillaPlaceStatus = apostamiento.getInt("plantilla_place_status");
            this.plantillaPlaceConsExp = apostamiento.getString("plantilla_place_cons_exp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Apostamiento update(JSONObject apostamiento){
        try {
            this.plantillaPlaceId = apostamiento.getInt("plantilla_place_id");
            this.plantillaPlaceClientId = apostamiento.getString("plantilla_place_client_id");
            this.plantillaPlaceApostamientoName = apostamiento.getString("plantilla_place_apostamiento_name");
            this.plantillaPlaceApostamientoAlias = apostamiento.getString("plantilla_place_apostamiento_alias");
            this.plantillaPlaceType = apostamiento.getString("plantilla_place_type");
            this.plantillaPlaceSiteId = apostamiento.getInt("plantilla_place_site_id");
            this.plantillaPlaceGuardsRequired = apostamiento.getInt("plantilla_place_guards_required");
            this.plantillaPlaceStatus = apostamiento.getInt("plantilla_place_status");
            this.plantillaPlaceConsExp = apostamiento.getString("plantilla_place_cons_exp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JSONObject mapData() {
        JSONObject data = new JSONObject();
        try {
            data.put("plantilla_place_id", this.plantillaPlaceId);
            data.put("plantilla_place_client_id", this.plantillaPlaceClientId);
            data.put("plantilla_place_apostamiento_name", this.plantillaPlaceApostamientoName);
            data.put("plantilla_place_apostamiento_alias", this.plantillaPlaceApostamientoAlias);
            data.put("plantilla_place_type", this.plantillaPlaceType);
            data.put("plantilla_place_site_id", this.plantillaPlaceSiteId);
            data.put("plantilla_place_guards_required",this.plantillaPlaceGuardsRequired);
            data.put("plantilla_place_status",this.plantillaPlaceStatus);
            data.put("plantilla_place_cons_exp",this.plantillaPlaceConsExp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getPlantillaPlaceId() {
        return plantillaPlaceId;
    }

    public void setPlantillaPlaceId(int plantillaPlaceId) {
        this.plantillaPlaceId = plantillaPlaceId;
    }

    public String getPlantillaPlaceClientId() {
        return plantillaPlaceClientId;
    }

    public void setPlantillaPlaceClientId(String plantillaPlaceClientId) {
        this.plantillaPlaceClientId = plantillaPlaceClientId;
    }

    public String getPlantillaPlaceApostamientoName() {
        return plantillaPlaceApostamientoName;
    }

    public void setPlantillaPlaceApostamientoName(String plantillaPlaceApostamientoName) {
        this.plantillaPlaceApostamientoName = plantillaPlaceApostamientoName;
    }

    public String getPlantillaPlaceApostamientoAlias() {
        return plantillaPlaceApostamientoAlias;
    }

    public void setPlantillaPlaceApostamientoAlias(String plantillaPlaceApostamientoAlias) {
        this.plantillaPlaceApostamientoAlias = plantillaPlaceApostamientoAlias;
    }

    public String getPlantillaPlaceType() {
        return plantillaPlaceType;
    }

    public void setPlantillaPlaceType(String plantillaPlaceType) {
        this.plantillaPlaceType = plantillaPlaceType;
    }

    public int getPlantillaPlaceSiteId() {
        return plantillaPlaceSiteId;
    }

    public void setPlantillaPlaceSiteId(int plantillaPlaceSiteId) {
        this.plantillaPlaceSiteId = plantillaPlaceSiteId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getPlantillaPlaceGuardsRequired() {
        return plantillaPlaceGuardsRequired;
    }

    public void setPlantillaPlaceGuardsRequired(int plantillaPlaceGuardsRequired) {
        this.plantillaPlaceGuardsRequired = plantillaPlaceGuardsRequired;
    }

    public int getPlantillaPlaceStatus() {
        return plantillaPlaceStatus;
    }

    public void setPlantillaPlaceStatus(int plantillaPlaceStatus) {
        this.plantillaPlaceStatus = plantillaPlaceStatus;
    }

    public String getPlantillaPlaceConsExp() {
        return plantillaPlaceConsExp;
    }

    public void setPlantillaPlaceConsExp(String plantillaPlaceConsExp) {
        this.plantillaPlaceConsExp = plantillaPlaceConsExp;
    }
}
