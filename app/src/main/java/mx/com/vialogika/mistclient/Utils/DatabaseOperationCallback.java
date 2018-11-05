package mx.com.vialogika.mistclient.Utils;

import org.jetbrains.annotations.Nullable;

public interface DatabaseOperationCallback {
    void onOperationSucceded(@Nullable Object response);
}
