package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.mistclient.Utils.ReportShareDialog;

@Entity(tableName = "EdoReports")
public class EdoReport {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int id;
    private String edoFuerzaPlantillaId;
    private String edoFuerzaProviderId;
    private String edoFuerzaSiteId;
    private String edoFuerzaPlaceId;
    private String edoFuerzaGuardId;
    private String edoFuerzaTiempo;
    private String edoFuerzaIncidenceId;
    private String edoFuerzaCoveredGuardId;
    private String edoFuerzaGuardJob;
    private String edoFuerzaDate;
    private String edoFuerzaReported;
    private String edoFuerzaTurno;

    public EdoReport() {

    }

    public EdoReport(JSONObject edoreport) {
        try {
            this.id = edoreport.getInt("id");
            this.edoFuerzaPlantillaId = edoreport.getString("edo_fuerza_plantilla_id");
            this.edoFuerzaProviderId = edoreport.getString("edo_fuerza_provider_id");
            this.edoFuerzaSiteId = edoreport.getString("edo_fuerza_site_id");
            this.edoFuerzaPlaceId = edoreport.getString("edo_fuerza_place_id");
            this.edoFuerzaGuardId = edoreport.getString("edo_fuerza_guard_id");
            this.edoFuerzaTiempo = edoreport.getString("edo_fuerza_tiempo");
            this.edoFuerzaIncidenceId = edoreport.getString("edo_fuerza_incidence_id");
            this.edoFuerzaCoveredGuardId = edoreport.getString("edo_fuerza_covered_guard_id");
            this.edoFuerzaGuardJob = edoreport.getString("edo_fuerza_guard_job");
            this.edoFuerzaDate = edoreport.getString("edo_fuerza_date");
            this.edoFuerzaReported = edoreport.getString("edo_fuerza_reported");
            this.edoFuerzaTurno = edoreport.getString("edo_fuerza_turno");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject mapData() {
        JSONObject edoreport = new JSONObject();
        try {
            edoreport.put("id", this.id);
            edoreport.put("edo_fuerza_plantilla_id", this.edoFuerzaPlantillaId);
            edoreport.put("edo_fuerza_provider_id", this.edoFuerzaProviderId);
            edoreport.put("edo_fuerza_site_id", this.edoFuerzaSiteId);
            edoreport.put("edo_fuerza_place_id", this.edoFuerzaPlaceId);
            edoreport.put("edo_fuerza_guard_id", this.edoFuerzaGuardId);
            edoreport.put("edo_fuerza_tiempo", this.edoFuerzaTiempo);
            edoreport.put("edo_fuerza_incidence_id", this.edoFuerzaIncidenceId);
            edoreport.put("edo_fuerza_covered_guard_id", this.edoFuerzaCoveredGuardId);
            edoreport.put("edo_fuerza_guard_job", this.edoFuerzaGuardJob);
            edoreport.put("edo_fuerza_date", this.edoFuerzaDate);
            edoreport.put("edo_fuerza_reported", this.edoFuerzaReported);
            edoreport.put("edo_fuerza_turno", this.edoFuerzaTurno);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return edoreport;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdoFuerzaPlantillaId() {
        return edoFuerzaPlantillaId;
    }

    public void setEdoFuerzaPlantillaId(String edoFuerzaPlantillaId) {
        this.edoFuerzaPlantillaId = edoFuerzaPlantillaId;
    }

    public String getEdoFuerzaProviderId() {
        return edoFuerzaProviderId;
    }

    public void setEdoFuerzaProviderId(String edoFuerzaProviderId) {
        this.edoFuerzaProviderId = edoFuerzaProviderId;
    }

    public String getEdoFuerzaSiteId() {
        return edoFuerzaSiteId;
    }

    public void setEdoFuerzaSiteId(String edoFuerzaSiteId) {
        this.edoFuerzaSiteId = edoFuerzaSiteId;
    }

    public String getEdoFuerzaPlaceId() {
        return edoFuerzaPlaceId;
    }

    public void setEdoFuerzaPlaceId(String edoFuerzaPlaceId) {
        this.edoFuerzaPlaceId = edoFuerzaPlaceId;
    }

    public String getEdoFuerzaGuardId() {
        return edoFuerzaGuardId;
    }

    public void setEdoFuerzaGuardId(String edoFuerzaGuardId) {
        this.edoFuerzaGuardId = edoFuerzaGuardId;
    }

    public String getEdoFuerzaTiempo() {
        return edoFuerzaTiempo;
    }

    public void setEdoFuerzaTiempo(String edoFuerzaTiempo) {
        this.edoFuerzaTiempo = edoFuerzaTiempo;
    }

    public String getEdoFuerzaIncidenceId() {
        return edoFuerzaIncidenceId;
    }

    public void setEdoFuerzaIncidenceId(String edoFuerzaIncidenceId) {
        this.edoFuerzaIncidenceId = edoFuerzaIncidenceId;
    }

    public String getEdoFuerzaCoveredGuardId() {
        return edoFuerzaCoveredGuardId;
    }

    public void setEdoFuerzaCoveredGuardId(String edoFuerzaCoveredGuardId) {
        this.edoFuerzaCoveredGuardId = edoFuerzaCoveredGuardId;
    }

    public String getEdoFuerzaGuardJob() {
        return edoFuerzaGuardJob;
    }

    public void setEdoFuerzaGuardJob(String edoFuerzaGuardJob) {
        this.edoFuerzaGuardJob = edoFuerzaGuardJob;
    }

    public String getEdoFuerzaDate() {
        return edoFuerzaDate;
    }

    public void setEdoFuerzaDate(String edoFuerzaDate) {
        this.edoFuerzaDate = edoFuerzaDate;
    }

    public String getEdoFuerzaReported() {
        return edoFuerzaReported;
    }

    public void setEdoFuerzaReported(String edoFuerzaReported) {
        this.edoFuerzaReported = edoFuerzaReported;
    }

    public String getEdoFuerzaTurno() {
        return edoFuerzaTurno;
    }

    public void setEdoFuerzaTurno(String edoFuerzaTurno) {
        this.edoFuerzaTurno = edoFuerzaTurno;
    }
}
