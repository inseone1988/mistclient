package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "GuardsForceState")
public class GuardForceState {

    @PrimaryKey(autoGenerate = true)
    private int localId;

    private int    id;
    private String edoFuerzaPlantillaId;
    private String edoFuerzaProviderId;
    private String edoFuerzaSiteId;
    private String edoFuerzaPlaceId;
    private String edoFuerzaGuardId;
    private String edoFuerzaIncidenceId;
    private String edoFuerzaCoveredGuardId;
    private String edoFuerzaGuardJob;
    private String edoFuerzaDate;
    private String edoFuerzaReported;
    private String edoFuerzaTurno;
    private String providerIncidencesUuid;
    private int    providerIncidencesId;
    private String providerIncidencesDatetime;
    private String providerIncidencesName;
    private String providerIncidencesType;
    private String providerIncidencesRequestedby;
    private String providerIncidencesObs;
    @Ignore
    private String guardName;
    @Ignore
    private String apName;

    public GuardForceState() {

    }

    public GuardForceState(JSONObject item) {
        try {
            String piid = item.getString("provider_incidences_id");
            this.id                            = item.getInt("id");
            this.edoFuerzaPlantillaId          = item.getString("edo_fuerza_plantilla_id"        );
            this.edoFuerzaProviderId           = item.getString("edo_fuerza_provider_id"         );
            this.edoFuerzaSiteId               = item.getString("edo_fuerza_site_id"             );
            this.edoFuerzaPlaceId              = item.getString("edo_fuerza_place_id"            );
            this.edoFuerzaGuardId              = item.getString("edo_fuerza_guard_id"            );
            this.edoFuerzaIncidenceId          = item.getString("edo_fuerza_incidence_id"        );
            this.edoFuerzaCoveredGuardId       = item.getString("edo_fuerza_covered_guard_id"    );
            this.edoFuerzaGuardJob             = item.getString("edo_fuerza_guard_job"           );
            this.edoFuerzaDate                 = item.getString("edo_fuerza_date"                );
            this.edoFuerzaReported             = item.getString("edo_fuerza_reported"            );
            this.edoFuerzaTurno                = item.getString("edo_fuerza_turno"               );
            this.providerIncidencesUuid        = item.getString("provider_incidences_uuid"       );
            this.providerIncidencesId          = piid.equals("null") ? 0 : item.getInt("provider_incidences_id");
            this.providerIncidencesDatetime    = item.getString("provider_incidences_datetime"   );
            this.providerIncidencesName        = item.getString("provider_incidences_name"       );
            this.providerIncidencesType        = item.getString("provider_incidences_type"       );
            this.providerIncidencesRequestedby = item.getString("provider_incidences_requestedby");
            this.providerIncidencesObs         = item.getString("provider_incidences_obs"        );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject mapData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", this.id                                                        );
            obj.put("edo_fuerza_plantilla_id", this.edoFuerzaPlantillaId                 );
            obj.put("edo_fuerza_provider_id", this.edoFuerzaProviderId                   );
            obj.put("edo_fuerza_site_id", this.edoFuerzaSiteId                           );
            obj.put("edo_fuerza_place_id", this.edoFuerzaPlaceId                         );
            obj.put("edo_fuerza_guard_id", this.edoFuerzaGuardId                         );
            obj.put("edo_fuerza_incidence_id", this.edoFuerzaIncidenceId                 );
            obj.put("edo_fuerza_covered_guard_id", this.edoFuerzaCoveredGuardId          );
            obj.put("edo_fuerza_guard_job", this.edoFuerzaGuardJob                       );
            obj.put("edo_fuerza_date", this.edoFuerzaDate                                );
            obj.put("edo_fuerza_reported", this.edoFuerzaReported                        );
            obj.put("edo_fuerza_turno", this.edoFuerzaTurno                              );
            obj.put("provider_incidences_uuid", this.providerIncidencesUuid              );
            obj.put("provider_incidences_id", this.providerIncidencesId                  );
            obj.put("provider_incidences_datetime", this.providerIncidencesDatetime      );
            obj.put("provider_incidences_name", this.providerIncidencesName              );
            obj.put("provider_incidences_type", this.providerIncidencesType              );
            obj.put("provider_incidences_requestedby", this.providerIncidencesRequestedby);
            obj.put("provider_incidences_obs", this.providerIncidencesObs                );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean hasIncidence(){
        return !this.edoFuerzaIncidenceId.equals("null");
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

    public String getProviderIncidencesUuid() {
        return providerIncidencesUuid;
    }

    public void setProviderIncidencesUuid(String providerIncidencesUuid) {
        this.providerIncidencesUuid = providerIncidencesUuid;
    }

    public int getProviderIncidencesId() {
        return providerIncidencesId;
    }

    public void setProviderIncidencesId(int providerIncidencesId) {
        this.providerIncidencesId = providerIncidencesId;
    }

    public String getProviderIncidencesDatetime() {
        return providerIncidencesDatetime;
    }

    public void setProviderIncidencesDatetime(String providerIncidencesDatetime) {
        this.providerIncidencesDatetime = providerIncidencesDatetime;
    }

    public String getProviderIncidencesName() {
        return providerIncidencesName;
    }

    public void setProviderIncidencesName(String providerIncidencesName) {
        this.providerIncidencesName = providerIncidencesName;
    }

    public String getProviderIncidencesType() {
        return providerIncidencesType;
    }

    public void setProviderIncidencesType(String providerIncidencesType) {
        this.providerIncidencesType = providerIncidencesType;
    }

    public String getProviderIncidencesRequestedby() {
        return providerIncidencesRequestedby;
    }

    public void setProviderIncidencesRequestedby(String providerIncidencesRequestedby) {
        this.providerIncidencesRequestedby = providerIncidencesRequestedby;
    }

    public String getProviderIncidencesObs() {
        return providerIncidencesObs;
    }

    public void setProviderIncidencesObs(String providerIncidencesObs) {
        this.providerIncidencesObs = providerIncidencesObs;
    }

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public int getProviderId(){
        return Integer.valueOf(this.edoFuerzaProviderId);
    }

    public String reportHour(){
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date  hour = format.parse(this.edoFuerzaDate);
            return new SimpleDateFormat("HH:mm:ss").format(hour);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return "";
    }
}
