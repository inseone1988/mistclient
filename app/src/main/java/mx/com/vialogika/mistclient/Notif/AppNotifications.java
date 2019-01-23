package mx.com.vialogika.mistclient.Notif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.json.JSONObject;

import java.util.List;

import mx.com.vialogika.mistclient.DscMainActivity;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Reporte;

public class AppNotifications {

    final public static int    REPORT_NOTIFICATION_HANDLE   = 12345;
    final public static int    DOWNLOAD_NOTIFICATION_HANDLE = 12515;
    final public static int    TESTIN_NOTIFICATION_HANDLE   = 67891;
    final public static String REPORT_GROUP                 = "REPORT_GROUP";

    private int PROGRESS_MAX     = 100;
    private int PROGRESS_CURRENT = 0;

    private Context ctx;

    private NotificationCompat.Builder DownloadNotification;

    public AppNotifications(Context context) {
        this.ctx = context;
    }

    public void reportNotification() {

    }

    private void reportLowLevelNotification() {

    }

    public NotificationCompat.Builder testingNotofication() {
        return new NotificationCompat.Builder(ctx, RegisterNotificationChannels.REPORTES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_dhl_icon)
                .setContentTitle("Hello")
                .setContentText("Report has arrived")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Report has arrived"));
    }

    public NotificationCompat.Builder newReportNotification(List<Reporte> reportes) {
        Intent intent = new Intent(ctx, DscMainActivity.class);
        intent.putExtra("loadReportsFragment", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
        String        reportText    = String.format(ctx.getResources().getString(R.string.report_notif_title), String.valueOf(reportes.size()));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, RegisterNotificationChannels.REPORTES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_dhl_icon)
                .setContentTitle(reportText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText("New reports");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("New reports arrived");
        for (int i = 0; i < reportes.size(); i++) {
            inboxStyle.addLine(reportes.get(i).getEventWhat());
        }
        mBuilder.setStyle(inboxStyle);
        return mBuilder;
    }

    public NotificationCompat.Builder newEdoFuerzaRported() {
        //TODO:Add edo intent,dar cuerpo a notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, RegisterNotificationChannels.EDO_FUERZA_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_dhl_icon)
                .setContentTitle("Hello");
        return builder;
    }

    private void initDownloadNotification() {
        DownloadNotification = new NotificationCompat.Builder(ctx, RegisterNotificationChannels.DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_dhl_icon)
                .setContentTitle("Descarga de imagen")
                .setContentText("Descarga en curso")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(getPROGRESS_MAX(),getPROGRESS_CURRENT(),true);
    }

    public NotificationCompat.Builder downloadNotification(){
        initDownloadNotification();
        return DownloadNotification;
    }

    public int getPROGRESS_MAX() {
        return PROGRESS_MAX;
    }

    public void setPROGRESS_MAX(int PROGRESS_MAX) {
        this.PROGRESS_MAX = PROGRESS_MAX;
    }

    public int getPROGRESS_CURRENT() {
        return PROGRESS_CURRENT;
    }

    public void setPROGRESS_CURRENT(int PROGRESS_CURRENT) {
        this.PROGRESS_CURRENT = PROGRESS_CURRENT;
    }

    public NotificationCompat.Builder getDownloadNotification() {
        return DownloadNotification;
    }
}

