package mx.com.vialogika.mistclient.Notif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.json.JSONObject;

import mx.com.vialogika.mistclient.R;

public class AppNotifications {

    final public static int REPORT_NOTIFICATION_HANDLE = 12345;
    final public static int TESTIN_NOTIFICATION_HANDLE = 67891;

    private Context ctx;

    public AppNotifications(Context context, JSONObject report){
        this.ctx = context;
    }

    public void reportNotification(){

    }

    private void reportLowLevelNotification(){

    }

    public NotificationCompat.Builder testingNotofication(){
        return  new NotificationCompat.Builder(ctx,RegisterNotificationChannels.REPORTES_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Hello")
                .setContentText("Report has arrived")
                .setStyle(new NotificationCompat.InboxStyle()
                .addLine("Hello"));
    }
}

