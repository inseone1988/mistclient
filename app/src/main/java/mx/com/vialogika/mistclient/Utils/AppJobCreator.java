package mx.com.vialogika.mistclient.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class AppJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag){
            case ReportSyncJob.TAG:
                return new ReportSyncJob();
            default:
                return null;
        }
    }
}
