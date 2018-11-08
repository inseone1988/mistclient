package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.Reporte;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;

public class DatabaseOperations {
    Context ctx ;
    private AppDatabase appDatabase;
    private List<Reporte> queue = new ArrayList<>();

    public DatabaseOperations(Context context){
        this.ctx = context;
        init();
    }

    private void init(){
        appDatabase = Room.databaseBuilder(ctx,AppDatabase.class,"Database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void addReportToQueue(Reporte reporte){
        queue.add(reporte);
    }

    public void getAllReports(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onOperationSucceded(appDatabase.reportDao().getAllReports());
            }
        }).start();
    }

    public void saveReport(final Reporte reporte){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.reportDao().saveReport(reporte);
            }
        }).start();
    }

    public void addReportsToQueue(List<Reporte> reportes){
        queue.addAll(reportes);
    }

    public void saveReports(){
        if(queue != null && queue.size() > 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    appDatabase.reportDao().saveReports(queue);
                    //Empty the queue to avoid multiple inserts
                    queue.clear();
                }
            }).start();

        }
    }

    public void getLastReportId(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int lastid = appDatabase.reportDao().fetchLastReportID();
                cb.onOperationSucceded(lastid);
            }
        }).start();
    }

    public void resetReportsTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.reportDao().eraseReportTable();
            }
        }).start();
    }

    //Comments related operations
    public void loadLocalComments(final int eventId, final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Comment> localComments = appDatabase.commentDao().getEventComments(eventId);
                cb.onOperationSucceded(localComments);
            }
        }).start();
    }

    public void saveComments(final List<Comment> comment,final @Nullable DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.commentDao().saveComments(comment);
                if (cb != null){
                    cb.onOperationSucceded(null);
                }
            }
        }).start();
    }

    public void close(){
        appDatabase.close();
    }
}
