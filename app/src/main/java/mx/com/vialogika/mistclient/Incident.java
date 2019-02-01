package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Incidents")
public class Incident {
    @PrimaryKey(autoGenerate = true)
    private int     localId;
    private int     eventId;
    private String  eventCaptureTimestamp;
    private String  eventDate;
    private String  eventTime;
    private String  eventName;
    private String  eventRiskLevel;
    private String  eventResponsable;
    private String  eventEvidence = "";
    private String  eventWhat;
    private String  eventHow;
    private String  eventWhen;
    private String  eventWhere;
    private String  eventFacts;
    private String  eventFiles;
    private String  eventUser;
    private String  eventUserSite;
    private boolean eventEditStatus;
    private String  eventSignatureNames;
    private String  eventSignature;
    private String  eventSignatureRoles;
    private String  eventType;
    private String  eventUUID;
    private String  eventUserPosition;
    private String  eventSiteClient;
    private String  eventSubject;
    private boolean eventStatus;
    private String  eventFlags;


    public Incident() {

    }

    public Incident(JSONObject incident) {
        try {
            this.eventId = incident.getInt("event_id");
            this.eventCaptureTimestamp = incident.getString("event_capture_timestamp");
            this.eventDate = incident.getString("event_date");
            this.eventTime = incident.getString("event_time");
            this.eventName = incident.getString("event_name");
            this.eventRiskLevel = incident.getString("event_risk_level");
            this.eventResponsable = incident.getString("event_responsable");
            this.eventEvidence = incident.getString("event_evidence");
            this.eventWhat = incident.getString("event_what");
            this.eventHow = incident.getString("event_how");
            this.eventWhen = incident.getString("event_when");
            this.eventWhere = incident.getString("event_where");
            this.eventFacts = incident.getString("event_facts");
            this.eventFiles = incident.getString("event_files");
            this.eventUser = incident.getString("event_user");
            this.eventUserSite = incident.getString("event_user_site");
            this.eventEditStatus = incident.getBoolean("event_edit_status");
            this.eventSignatureNames = incident.getString("event_signature_names");
            this.eventSignature = incident.getString("event_signature");
            this.eventSignatureRoles = incident.getString("event_signature_roles");
            this.eventType = incident.getString("event_type");
            this.eventUUID = incident.getString("event_UUID");
            this.eventUserPosition = incident.getString("event_user_position");
            this.eventSiteClient = incident.getString("event_site_client");
            this.eventSubject = incident.getString("event_subject");
            this.eventStatus = incident.getBoolean("event_status");
            this.eventFlags = incident.getString("event_flags");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject incident = new JSONObject();
        try {
            incident.put("event_id", this.eventId);
            incident.put("event_capture_timestamp", this.eventCaptureTimestamp);
            incident.put("event_date", this.eventDate);
            incident.put("event_time", this.eventTime);
            incident.put("event_name", this.eventName);
            incident.put("event_risk_level", this.eventRiskLevel);
            incident.put("event_responsable", this.eventResponsable);
            incident.put("event_evidence", this.eventEvidence);
            incident.put("event_what", this.eventWhat);
            incident.put("event_how", this.eventHow);
            incident.put("event_when", this.eventWhen);
            incident.put("event_where", this.eventWhere);
            incident.put("event_facts", this.eventFacts);
            incident.put("event_files", this.eventFiles);
            incident.put("event_user", this.eventUser);
            incident.put("event_user_site", this.eventUserSite);
            incident.put("event_edit_status", this.eventEditStatus);
            incident.put("event_signature_names", this.eventSignatureNames);
            incident.put("event_signature", this.eventSignature);
            incident.put("event_signature_roles", this.eventSignatureRoles);
            incident.put("event_type", this.eventType);
            incident.put("event_UUID", this.eventUUID);
            incident.put("event_user_position", this.eventUserPosition);
            incident.put("event_site_client", this.eventSiteClient);
            incident.put("event_subject", this.eventSubject);
            incident.put("event_status", this.eventStatus);
            incident.put("event_flags", this.eventFlags);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return incident;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCaptureTimestamp() {
        return eventCaptureTimestamp;
    }

    public void setEventCaptureTimestamp(String eventCaptureTimestamp) {
        this.eventCaptureTimestamp = eventCaptureTimestamp;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventRiskLevel() {
        return eventRiskLevel;
    }

    public void setEventRiskLevel(String eventRiskLevel) {
        this.eventRiskLevel = eventRiskLevel;
    }

    public String getEventResponsable() {
        return eventResponsable;
    }

    public void setEventResponsable(String eventResponsable) {
        this.eventResponsable = eventResponsable;
    }

    public String getEventEvidence() {
        return eventEvidence;
    }

    public void setEventEvidence(String eventEvidence) {
        this.eventEvidence = eventEvidence;
    }

    public String getEventWhat() {
        return eventWhat;
    }

    public void setEventWhat(String eventWhat) {
        this.eventWhat = eventWhat;
    }

    public String getEventHow() {
        return eventHow;
    }

    public void setEventHow(String eventHow) {
        this.eventHow = eventHow;
    }

    public String getEventWhen() {
        return eventWhen;
    }

    public void setEventWhen(String eventWhen) {
        this.eventWhen = eventWhen;
    }

    public String getEventWhere() {
        return eventWhere;
    }

    public void setEventWhere(String eventWhere) {
        this.eventWhere = eventWhere;
    }

    public String getEventFacts() {
        return eventFacts;
    }

    public void setEventFacts(String eventFacts) {
        this.eventFacts = eventFacts;
    }

    public String getEventFiles() {
        return eventFiles;
    }

    public void setEventFiles(String eventFiles) {
        this.eventFiles = eventFiles;
    }

    public String getEventUser() {
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public String getEventUserSite() {
        return eventUserSite;
    }

    public void setEventUserSite(String eventUserSite) {
        this.eventUserSite = eventUserSite;
    }

    public boolean isEventEditStatus() {
        return eventEditStatus;
    }

    public void setEventEditStatus(boolean eventEditStatus) {
        this.eventEditStatus = eventEditStatus;
    }

    public String getEventSignatureNames() {
        return eventSignatureNames;
    }

    public void setEventSignatureNames(String eventSignatureNames) {
        this.eventSignatureNames = eventSignatureNames;
    }

    public String getEventSignature() {
        return eventSignature;
    }

    public void setEventSignature(String eventSignature) {
        this.eventSignature = eventSignature;
    }

    public String getEventSignatureRoles() {
        return eventSignatureRoles;
    }

    public void setEventSignatureRoles(String eventSignatureRoles) {
        this.eventSignatureRoles = eventSignatureRoles;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventUUID() {
        return eventUUID;
    }

    public void setEventUUID(String eventUUID) {
        this.eventUUID = eventUUID;
    }

    public String getEventUserPosition() {
        return eventUserPosition;
    }

    public void setEventUserPosition(String eventUserPosition) {
        this.eventUserPosition = eventUserPosition;
    }

    public String getEventSiteClient() {
        return eventSiteClient;
    }

    public void setEventSiteClient(String eventSiteClient) {
        this.eventSiteClient = eventSiteClient;
    }

    public String getEventSubject() {
        return eventSubject;
    }

    public void setEventSubject(String eventSubject) {
        this.eventSubject = eventSubject;
    }

    public boolean isEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(boolean eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventFlags() {
        return eventFlags;
    }

    public void setEventFlags(String eventFlags) {
        this.eventFlags = eventFlags;
    }
}
