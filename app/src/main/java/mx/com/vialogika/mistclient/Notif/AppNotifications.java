package mx.com.vialogika.mistclient.Notif;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import org.json.JSONObject;

import mx.com.vialogika.mistclient.R;

public class AppNotifications {

    final public static int REPORT_NOTIFICATION_HANDLE = 12345;

    private Context ctx;

    public AppNotifications(Context context, JSONObject report){
        this.ctx = context;
    }

    public void reportNotification(){

    }

    private void reportLowLevelNotification(){

    }
}

