package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
                }
            }).start();

        }
    }
}
