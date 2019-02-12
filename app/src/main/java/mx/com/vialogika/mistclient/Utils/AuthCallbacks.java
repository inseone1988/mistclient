package mx.com.vialogika.mistclient.Utils;

import android.content.Context;


import android.support.annotation.Nullable;

public interface AuthCallbacks {
    void onAuthenticated();
    void onAuthenticatedFailed();
}
