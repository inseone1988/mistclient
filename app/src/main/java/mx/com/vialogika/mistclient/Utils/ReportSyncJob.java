package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mx.com.vialogika.mistclient.Notif.AppNotifications;
import mx.com.vialogika.mistclient.Reporte;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class ReportSyncJob extends Job {

    //Context ctx = this.getContext().getApplicationContext();

    public static final String TAG = "report_sync_job";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
       final Context ctx = this.getContext().getApplicationContext();
        DatabaseOperations dbo = new DatabaseOperations(ctx);
        dbo.syncReports(new DatabaseOperations.Sync() {
            @Override
            public void onReportSynced(List<Reporte> reports) {
                if (reports.size() > 0){
                    AppNotifications notification = new AppNotifications(ctx);
                    NotificationManagerCompat nManager = NotificationManagerCompat.from(ctx);
                    for (int i = 1; i < reports.size(); i++){
                        nManager.notify(AppNotifications.REPORT_NOTIFICATION_HANDLE,notification.newReportNotification(reports).build());
                    }
                }
            }
        });
        return Result.SUCCESS;
    }

    public static void scheduleJob(){
        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(ReportSyncJob.TAG);
        if(!jobRequests.isEmpty()){
            return;
        }
        new JobRequest.Builder(ReportSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15),TimeUnit.MINUTES.toMillis(7))
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
    }

    public static void runJobNow(){
        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(ReportSyncJob.TAG);
        if(!jobRequests.isEmpty()){
            return;
        }
        new JobRequest.Builder(ReportSyncJob.TAG)
                .startNow()
                //.setPeriodic(TimeUnit.MINUTES.toMillis(15),TimeUnit.MINUTES.toMillis(7))
                //.setUpdateCurrent(true)
                //.setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                //.setRequirementsEnforced(true)
                .build()
                .schedule();
    }
}
