package mx.com.vialogika.mistclient.Utils;

import com.android.volley.VolleyError;

public interface NetworkRequestCallbacks {
    void onNetworkRequestResponse(Object response);
    void onNetworkRequestError(VolleyError error);
}
