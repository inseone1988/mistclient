package mx.com.vialogika.mistclient.Notif;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import mx.com.vialogika.mistclient.R;

public class RegisterNotificationChannels {


    private Context ctx ;
    final static public String REPORTES_CHANNEL_ID = "REPORTES";

    public RegisterNotificationChannels(Context context){
        this.ctx = context;
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        createReportNotificationChannel();
    }

    private void createReportNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = ctx.getString(R.string.channel_name_report);
            String description = ctx.getString(R.string.channel_name_report_description_low);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(REPORTES_CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
