package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import org.jetbrains.annotations.Nullable;

public interface AuthCallbacks {
    void onAuthenticated();
    void onAuthenticatedFailed();
}
