package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Sites")
public class Site {
    @PrimaryKey(autoGenerate = true)
    private int internalSiteId;
    private int siteId;
    private String siteName;

    public Site() {
    }


    public Site(JSONObject siteObject){
        try{
            this.siteId = siteObject.getInt("site_id");
            this.siteName = siteObject.getString("site_name");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getInternalSiteId() {
        return internalSiteId;
    }

    public void setInternalSiteId(int internalSiteId) {
        this.internalSiteId = internalSiteId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
