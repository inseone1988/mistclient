package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSettings {

    private Context context;

    private int userId;
    private String managesSites;
    private boolean canArchiveReports;
    private boolean canShareReports;
    private boolean canFlagReports;
    private boolean canUpdateEdo;
    private boolean canAddGuards;
    private boolean canAddApts;
    private boolean canDeleteGuards;
    private boolean canaDeleteApts;
    private String defaultMails;

    public UserSettings(Context context){
        this.context = context;
        retrieve();
    }

    public void mapAndroidSettings(JSONObject as){
        try{
            this.userId = as.getInt("user_id");
            this.managesSites = as.getString("can_access_sites_data");
            this.canArchiveReports = as.getBoolean("can_archive_reports");
            this.canShareReports = as.getBoolean("can_share_reports");
            this.canFlagReports = as.getBoolean("can_flag_reports");
            this.canUpdateEdo = as.getBoolean("can_update_edo");
            this.canAddGuards = as.getBoolean("can_add_guards");
            this.canAddApts = as.getBoolean("can_add_apts");
            this.canDeleteGuards = as.getBoolean("can_delete_guards");
            this.canaDeleteApts = as.getBoolean("can_delete_apts");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void save(){
        SharedPreferences sp = context.getSharedPreferences("AndroidSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("uid",userId);
        editor.putString("casd",managesSites);
        editor.putBoolean("car",canArchiveReports);
        editor.putBoolean("csr",canShareReports);
        editor.putBoolean("cfr",canFlagReports);
        editor.putBoolean("cue",canUpdateEdo);
        editor.putBoolean("cag",canAddGuards);
        editor.putBoolean("caap",canAddApts);
        editor.putBoolean("cdg",canDeleteGuards);
        editor.putBoolean("cda",canaDeleteApts);
        editor.apply();
    }

    public void retrieve(){
        SharedPreferences sp = context.getSharedPreferences("AndroidSettings",Context.MODE_PRIVATE);
        this.userId = sp.getInt("uid",0);
        this.managesSites = sp.getString("casd","undefined");
        this.canArchiveReports = sp.getBoolean("car",false);
        this.canShareReports = sp.getBoolean("csr",false);
        this.canFlagReports = sp.getBoolean("cfr",false);
        this.canUpdateEdo = sp.getBoolean("cue",false);
        this.canAddGuards = sp.getBoolean("cag",false);
        this.canAddApts = sp.getBoolean("caap",false);
        this.canDeleteGuards = sp.getBoolean("cdg",false);
        this.canaDeleteApts = sp.getBoolean("cda",false);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getManagesSites() {
        return managesSites;
    }

    public void setManagesSites(String managesSites) {
        this.managesSites = managesSites;
    }

    public boolean isCanArchiveReports() {
        return canArchiveReports;
    }

    public void setCanArchiveReports(boolean canArchiveReports) {
        this.canArchiveReports = canArchiveReports;
    }

    public boolean isCanShareReports() {
        return canShareReports;
    }

    public void setCanShareReports(boolean canShareReports) {
        this.canShareReports = canShareReports;
    }

    public boolean isCanFlagReports() {
        return canFlagReports;
    }

    public void setCanFlagReports(boolean canFlagReports) {
        this.canFlagReports = canFlagReports;
    }

    public boolean isCanUpdateEdo() {
        return canUpdateEdo;
    }

    public void setCanUpdateEdo(boolean canUpdateEdo) {
        this.canUpdateEdo = canUpdateEdo;
    }

    public boolean isCanAddGuards() {
        return canAddGuards;
    }

    public void setCanAddGuards(boolean canAddGuards) {
        this.canAddGuards = canAddGuards;
    }

    public boolean isCanAddApts() {
        return canAddApts;
    }

    public void setCanAddApts(boolean canAddApts) {
        this.canAddApts = canAddApts;
    }

    public boolean isCanDeleteGuards() {
        return canDeleteGuards;
    }

    public void setCanDeleteGuards(boolean canDeleteGuards) {
        this.canDeleteGuards = canDeleteGuards;
    }

    public boolean isCanaDeleteApts() {
        return canaDeleteApts;
    }

    public void setCanaDeleteApts(boolean canaDeleteApts) {
        this.canaDeleteApts = canaDeleteApts;
    }

    public boolean managesMoreThanOneSite(){
        return managesSites.split(",").length > 1;
    }
}
