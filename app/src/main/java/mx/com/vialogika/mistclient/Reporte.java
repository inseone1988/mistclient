package mx.com.vialogika.mistclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Reporte implements Serializable {

    private int reportId;
    private String reportTitle;
    private String reportTimeStamp;
    private String reportExplanation;
    private String reportGrade;

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
}
