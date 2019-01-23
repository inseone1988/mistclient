package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Guards")
public class Guard {
    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int idpersons;
    private String personCreated;
    private int personProviderid;
    private String personType;
    private String personPosition;
    private String personName;
    private String personFname;
    private String personLname;
    private String personProfilePhotoPath;
    private int guardId;
    private int guardProviderId;
    private int guardPersonId;
    private String guardSite;
    private String guardRange;
    private String guardHash;
    private String guardPhotoPath;
    private int guardStatus;
    private String guardBajaTimestamp;
    @Ignore
    private String guardFullname;

    public Guard() {
    }

    public Guard(JSONObject guard) {
        try {
            this.idpersons = guard.getInt("idpersons");
            this.personCreated = guard.getString("person_created");
            this.personProviderid = guard.getInt("person_providerid");
            this.personType = guard.getString("person_type");
            this.personPosition = guard.getString("person_position");
            this.personName = guard.getString("person_name");
            this.personFname = guard.getString("person_fname");
            this.personLname = guard.getString("person_lname");
            this.personProfilePhotoPath = guard.getString("person_profile_photo_path");
            this.guardId = guard.getInt("guard_id");
            this.guardProviderId = guard.getInt("guard_provider_id");
            this.guardPersonId = guard.getInt("guard_person_id");
            this.guardSite = guard.getString("guard_site");
            this.guardRange = guard.getString("guard_range");
            this.guardHash = guard.getString("guard_hash");
            this.guardPhotoPath = guard.getString("guard_photo_path");
            this.guardStatus = guard.getInt("guard_status");
            this.guardBajaTimestamp = guard.getString("guard_baja_timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void upadteGuard(Guard guard){
        this.idpersons = guard.getIdpersons();
        this.personCreated = getPersonCreated();
        this.personProviderid = guard.getPersonProviderid();
        this.personType = guard.personType;
        this.personPosition = guard.getPersonPosition();
        this.personName = guard.getPersonName();
        this.personFname = guard.getPersonFname();
        this.personLname = guard.getPersonLname();
        this.personProfilePhotoPath = getPersonProfilePhotoPath();
        this.guardId = guard.getGuardId();
        this.guardProviderId = guard.getGuardProviderId();
        this.guardPersonId = guard.getGuardPersonId();
        this.guardSite = guard.getGuardSite();
        this.guardRange = guard.getGuardRange();
        this.guardHash = guard.getGuardHash();
        this.guardPhotoPath = getGuardPhotoPath();
        this.guardStatus = guard.getGuardStatus();
        this.guardBajaTimestamp = guard.getGuardBajaTimestamp();
    }

    public JSONObject mapData() {
        JSONObject data = new JSONObject();
        try {
            data.put("idpersons", this.idpersons);
            data.put("person_created", this.personCreated);
            data.put("person_providerid", this.personProviderid);
            data.put("person_type", this.personType);
            data.put("person_position", this.personPosition);
            data.put("person_name", this.personName);
            data.put("person_fname", this.personFname);
            data.put("person_lname", this.personLname);
            data.put("person_profile_photo_path", this.personProfilePhotoPath);
            data.put("guard_id", this.guardId);
            data.put("guard_provider_id", this.guardProviderId);
            data.put("guard_person_id", this.guardPersonId);
            data.put("guard_site", this.guardSite);
            data.put("guard_range", this.guardRange);
            data.put("guard_hash", this.guardHash);
            data.put("guard_photo_path", this.guardPhotoPath);
            data.put("guard_status", this.guardStatus);
            data.put("guard_baja_timestamp", this.guardBajaTimestamp);
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

    public int getIdpersons() {
        return idpersons;
    }

    public void setIdpersons(int idpersons) {
        this.idpersons = idpersons;
    }

    public String getPersonCreated() {
        return personCreated;
    }

    public void setPersonCreated(String personCreated) {
        this.personCreated = personCreated;
    }

    public int getPersonProviderid() {
        return personProviderid;
    }

    public void setPersonProviderid(int personProviderid) {
        this.personProviderid = personProviderid;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonPosition() {
        return personPosition;
    }

    public void setPersonPosition(String personPosition) {
        this.personPosition = personPosition;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonFname() {
        return personFname;
    }

    public void setPersonFname(String personFname) {
        this.personFname = personFname;
    }

    public String getPersonLname() {
        return personLname;
    }

    public void setPersonLname(String personLname) {
        this.personLname = personLname;
    }

    public String getPersonProfilePhotoPath() {
        return personProfilePhotoPath;
    }

    public void setPersonProfilePhotoPath(String personProfilePhotoPath) {
        this.personProfilePhotoPath = personProfilePhotoPath;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public int getGuardProviderId() {
        return guardProviderId;
    }

    public void setGuardProviderId(int guardProviderId) {
        this.guardProviderId = guardProviderId;
    }

    public int getGuardPersonId() {
        return guardPersonId;
    }

    public void setGuardPersonId(int guardPersonId) {
        this.guardPersonId = guardPersonId;
    }

    public String getGuardSite() {
        return guardSite;
    }

    public void setGuardSite(String guardSite) {
        this.guardSite = guardSite;
    }

    public String getGuardRange() {
        return guardRange;
    }

    public void setGuardRange(String guardRange) {
        this.guardRange = guardRange;
    }

    public String getGuardHash() {
        return guardHash;
    }

    public void setGuardHash(String guardHash) {
        this.guardHash = guardHash;
    }

    public String getGuardPhotoPath() {
        return guardPhotoPath;
    }

    public void setGuardPhotoPath(String guardPhotoPath) {
        this.guardPhotoPath = guardPhotoPath;
    }

    public int getGuardStatus() {
        return guardStatus;
    }

    public void setGuardStatus(int guardStatus) {
        this.guardStatus = guardStatus;
    }

    public String getGuardBajaTimestamp() {
        return guardBajaTimestamp;
    }

    public void setGuardBajaTimestamp(String guardBajaTimestamp) {
        this.guardBajaTimestamp = guardBajaTimestamp;
    }

    public String getGuardFname(){
        return personName + " " + personFname + " " + personLname;
    }

    public boolean isActive(){
        return guardStatus == 1;
    }

    public String getGuardFullname() {
        return this.personName + " " + personFname + " " + personLname;
    }

    public boolean hasProfilePhoto(){
        return !this.personProfilePhotoPath.equals("");
    }

    public void setGuardFullname(String guardFullname) {
        this.guardFullname = guardFullname;
    }
}
