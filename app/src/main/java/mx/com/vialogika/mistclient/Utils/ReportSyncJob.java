package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ReportSyncJob extends Job {

    Context context = this.getContext();

    public static final String TAG = "report_sync_job";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        //run job here
        return null;
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
}
