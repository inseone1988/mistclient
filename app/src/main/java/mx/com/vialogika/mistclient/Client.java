package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Clients")
public class Client {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int clientId;
    private String clientSocial;
    private String clientName;
    private String clientAlias;
    private int clientSiteId;

    public Client() {
    }

    public Client(JSONObject client) {
        try {
            this.clientId = client.getInt("client_id");
            this.clientSocial = client.getString("client_social");
            this.clientName = client.getString("client_name");
            this.clientAlias = client.getString("client_alias");
            this.clientSiteId = client.getInt("client_site_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject mapData() {
        JSONObject client = new JSONObject();
        try {
            client.put("client_id", this.clientId);
            client.put("client_social", this.clientSocial);
            client.put("client_name", this.clientName);
            client.put("client_alias", this.clientAlias);
            client.put("client_site_id", this.clientSiteId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return client;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientSocial() {
        return clientSocial;
    }

    public void setClientSocial(String clientSocial) {
        this.clientSocial = clientSocial;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAlias() {
        return clientAlias;
    }

    public void setClientAlias(String clientAlias) {
        this.clientAlias = clientAlias;
    }

    public int getClientSiteId() {
        return clientSiteId;
    }

    public void setClientSiteId(int clientSiteId) {
        this.clientSiteId = clientSiteId;
    }
}