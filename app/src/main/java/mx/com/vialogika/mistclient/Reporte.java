package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "Reports")
public class Reporte implements Serializable
{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int reportId;

    private int remReportId;
    private String reportTitle;
    private String reportTimeStamp;
    private String reportExplanation;
    private String reportGrade;
    private String username;//Who redacts the report
    private String eventWhat;
    private String eventHow;
    private String eventWhere;
    private String stringEvidences;
    private String stringSignatures;
    private String stringSignatureNames;
    private String stringSignatureRoles;
    private String reportStatus = "active";

    public Reporte(){

    }

    public Reporte(JSONObject report){
        //TODO: Modify database to show more than 1000 rows
        try{
            this.remReportId = report.getInt("id");
            this.reportTitle = report.getString("name").equals("null") ? report.getString("what") : report.getString("name");
            this.reportTimeStamp = report.getString("etimestamp");
            this.reportExplanation = report.getString("exp");
            this.reportGrade = report.getString("risk");
            this.username = report.getString("user");
            this.eventWhat = report.getString("what");
            this.eventHow = report.getString("how");
            this.eventWhere = report.getString("ewhere");
            this.stringEvidences = report.getString("evidence");
            this.stringSignatures = report.getString("signatures");
            this.stringSignatureNames = report.getString("signaturenames");
            this.stringSignatureRoles = report.getString("sroles");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportTimeStamp() {
        return reportTimeStamp;
    }

    public void setReportTimeStamp(String reportTimeStamp) {
        this.reportTimeStamp = reportTimeStamp;
    }

    public String getReportExplanation() {
        return reportExplanation;
    }

    public void setReportExplanation(String reportExplanation) {
        this.reportExplanation = reportExplanation;
    }

    public String getReportGrade() {
        return reportGrade;
    }

    public void setReportGrade(String reportGrade) {
        this.reportGrade = reportGrade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEventWhere() {
        return eventWhere;
    }

    public void setEventWhere(String eventWhere) {
        this.eventWhere = eventWhere;
    }

    public boolean hasEvidences() {
        boolean has = false;
        if(stringEvidences != null){
            has = this.stringEvidences.split(",").length > 0;
        }
        return has ;
    }


    public boolean hasSignatures(){
        boolean has = false;
        if (this.stringSignatures != null){
            has =  this.stringSignatures.split(",").length > 0;
        }
        return has;
    }

    public String[] getEvidences() {
        return stringEvidences.split(",");
    }

    public String[] getSignatures() {
        return stringSignatures.split(",");
    }

    public String[] getSigantureNames() {
        return stringSignatureNames.split(",");
    }

    public String[] getSignaturesRoles() {
        return stringSignatureRoles.split(",");
    }

    public String getStringEvidences() {
        return stringEvidences;
    }

    public void setStringEvidences(String stringEvidences) {
        this.stringEvidences = stringEvidences;
    }

    public String getStringSignatures() {
        return stringSignatures;
    }

    public void setStringSignatures(String stringSignatures) {
        this.stringSignatures = stringSignatures;
    }

    public String getStringSignatureNames() {
        return stringSignatureNames;
    }

    public void setStringSignatureNames(String stringSignatureNames) {
        this.stringSignatureNames = stringSignatureNames;
    }

    public String getStringSignatureRoles() {
        return stringSignatureRoles;
    }

    public void setStringSignatureRoles(String stringSignatureRoles) {
        this.stringSignatureRoles = stringSignatureRoles;
    }

    public int getRemReportId() {
        return remReportId;
    }

    public void setRemReportId(int remReportId) {
        this.remReportId = remReportId;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }
}
