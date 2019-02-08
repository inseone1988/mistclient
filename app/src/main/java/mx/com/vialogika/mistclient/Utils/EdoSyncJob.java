package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mx.com.vialogika.mistclient.GuardForceState;
import mx.com.vialogika.mistclient.Notif.AppNotifications;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;
import mx.com.vialogika.mistclient.UserSettings;

public class EdoSyncJob extends Job {

    public static final String TAG = "edo_sync_job";
    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        final Context      context = this.getContext().getApplicationContext();
        DatabaseOperations dbo = new DatabaseOperations(context);
        UserSettings       settings = new UserSettings(context);
        Calendar           calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String from = sdf.format(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        String to  = sdf.format(calendar.getTime());
        if (settings.managesMoreThanOneSite()){
            String[] sites = settings.getManagesSites().split(",");
            for (int i = 0; i < sites.length; i++) {
                NetworkRequest.getEdoFuerza(context, from, to, Integer.valueOf(sites[i]), new DatabaseOperations.doInBackgroundOperation() {
                    @Override
                    public void onOperationFinished(@Nullable Object object) {
                    }
                }, new DatabaseOperations.UIThreadOperation() {
                    @Override
                    public void onOperationFinished(@Nullable Object object) {

                    }
                });
            }
        }else{
            NetworkRequest.getEdoFuerza(context, from, to, Integer.valueOf(settings.getManagesSites()), new DatabaseOperations.doInBackgroundOperation() {
                @Override
                public void onOperationFinished(@Nullable Object object) {

                }
            }, new DatabaseOperations.UIThreadOperation() {
                @Override
                public void onOperationFinished(@Nullable Object object) {

                }
            });
        }
        return Result.SUCCESS;
    }

    public static void scheduleJob(){
        Set<JobRequest> jobRequestSet = JobManager.instance().getAllJobRequestsForTag(EdoSyncJob.TAG);
        if (!jobRequestSet.isEmpty()){
            return;
        }
        new JobRequest.Builder(EdoSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15),TimeUnit.MINUTES.toMillis(7))
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
    }

    public static void runNowjob(){
        Set<JobRequest> jobRequestSet = JobManager.instance().getAllJobRequestsForTag(EdoSyncJob.TAG);
        if (!jobRequestSet.isEmpty()){
            return;
        }
        new JobRequest.Builder(EdoSyncJob.TAG)
                .startNow()
                //.setPeriodic(TimeUnit.MINUTES.toMillis(15),TimeUnit.MINUTES.toMillis(7))
                //.setUpdateCurrent(true)
                //.setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                //.setRequirementsEnforced(true)
                .build()
                .schedule();
    }
}
