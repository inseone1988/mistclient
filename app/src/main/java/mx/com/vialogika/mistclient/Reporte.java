package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "Reports")
public class Reporte implements Serializable
{

    @NonNull
    @PrimaryKey
    private int reportId;
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
    @Ignore
    private String[] evidences;
    @Ignore
    private String[] signatures;
    @Ignore
    private String[] sigantureNames;
    @Ignore
    private String[] signaturesRoles;


    public Reporte(){

    }

    public Reporte(JSONObject report){
        //TODO: Modify database to show more than 1000 rows
        try{
            this.reportId = report.getInt("id");
            this.reportTitle = report.getString("name").equals("null") ? report.getString("what") : report.getString("name");
            this.reportTimeStamp = report.getString("etimestamp");
            this.reportExplanation = report.getString("exp");
            this.reportGrade = report.getString("risk");
            this.username = report.getString("user");
            this.eventWhat = report.getString("what");
            this.eventHow = report.getString("how");
            this.eventWhere = report.getString("ewhere");
            this.stringEvidences = report.getString("evidence");
            this.stringSignatures = report.getString("sigantures");
            this.stringSignatureNames = report.getString("signaturenames");
            this.stringSignatureRoles = report.getString("sroles");

            //TODO: Remove this fields deprecated 05*11*2018
            this.evidences = report.getString("evidence").split(",");
            this.signatures = report.getString("signatures").split(",");
            this.sigantureNames = report.getString("signaturenames").split(",");
            this.signaturesRoles = report.getString("sroles").split(",");
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
        return this.evidences.length > 0 ;
    }

    public boolean hasSignatures(){
        return this.signatures.length > 0;
    }

    public String[] getEvidences() {
        return evidences;
    }

    public void setEvidences(String[] evidences) {
        this.evidences = evidences;
    }

    public String[] getSignatures() {
        return signatures;
    }

    public void setSignatures(String[] signatures) {
        this.signatures = signatures;
    }

    public String[] getSigantureNames() {
        return sigantureNames;
    }

    public void setSigantureNames(String[] sigantureNames) {
        this.sigantureNames = sigantureNames;
    }

    public String[] getSignaturesRoles() {
        return signaturesRoles;
    }

    public void setSignaturesRoles(String[] signaturesRoles) {
        this.signaturesRoles = signaturesRoles;
    }

}
