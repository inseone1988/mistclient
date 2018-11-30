package mx.com.vialogika.mistclient.Utils;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Providers")
public class Provider {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int providerId;
    private String providerCreated;
    private String providerAlias;
    private String providerRfc;
    private String providerSocial;
    private String providerClass;

    public Provider() {

    }

    public Provider(JSONObject provider) {
        try {
            this.providerId = provider.getInt("provider_id");
            this.providerCreated = provider.getString("provider_created");
            this.providerAlias = provider.getString("provider_alias");
            this.providerRfc = provider.getString("provider_rfc");
            this.providerSocial = provider.getString("provider_social");
            this.providerClass = provider.getString("provider_class");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject mapData() {
        JSONObject provider = new JSONObject();
        try {
            provider.put("provider_id", this.providerId);
            provider.put("provider_created", this.providerCreated);
            provider.put("provider_alias", this.providerAlias);
            provider.put("provider_rfc", this.providerRfc);
            provider.put("provider_social", this.providerSocial);
            provider.put("provider_class", this.providerClass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderCreated() {
        return providerCreated;
    }

    public void setProviderCreated(String providerCreated) {
        this.providerCreated = providerCreated;
    }

    public String getProviderAlias() {
        return providerAlias;
    }

    public void setProviderAlias(String providerAlias) {
        this.providerAlias = providerAlias;
    }

    public String getProviderRfc() {
        return providerRfc;
    }

    public void setProviderRfc(String providerRfc) {
        this.providerRfc = providerRfc;
    }

    public String getProviderSocial() {
        return providerSocial;
    }

    public void setProviderSocial(String providerSocial) {
        this.providerSocial = providerSocial;
    }

    public String getProviderClass() {
        return providerClass;
    }

    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }
}
