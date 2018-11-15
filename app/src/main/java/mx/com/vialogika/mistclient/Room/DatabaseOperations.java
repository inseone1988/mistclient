package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.Reporte;
import mx.com.vialogika.mistclient.User;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

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

    public void saveSites(final List<Site> sites){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.sitesDao().saveSites(sites);

            }
        }).start();
    }

    public void getSiteNames(final simpleOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
               List<Site> sites = appDatabase.sitesDao().getSitenames();
                cb.onOperationFinished(sites);

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

    public List<Reporte> syncReports(final Sync cb){
        final List<Reporte> fetched = new ArrayList<>();
        getLastReportId(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(Object response) {
                int lastid = (int) response;
                int userid = User.userId(ctx);
                int usersite = User.userSite(ctx);
                NetworkRequest.fetchIncidents(ctx, lastid, userid, usersite, new NetworkRequestCallbacks() {
                    @Override
                    public void onNetworkRequestResponse(Object response) {
                        try{
                            JSONObject resp = new JSONObject(response.toString());
                            if (resp.length() > 0){
                                if (resp.getBoolean("success")){
                                    JSONArray reports = resp.getJSONArray("reports");
                                    Gson gson = new Gson();
                                    if(reports.length() > 0){
                                        for (int i = 0;i < reports.length();i++){
                                            Reporte report = new Reporte(reports.getJSONObject(i));
                                            saveReport(report);
                                            fetched.add(report);
                                        }
                                    }
                                    //Callback
                                    cb.onReportSynced(fetched);
                                }
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNetworkRequestError(VolleyError error) {
                        error.printStackTrace();
                    }
                });
            }
        });
        return fetched;
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

    public void resetSitesTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.sitesDao().deleteSitesTable();
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

    public interface Sync{
        void onReportSynced(List<Reporte> reports);
    }

    public interface simpleOperationCallback{
        //Generic callback that accpts a result from background operation or generic type return
        void onOperationFinished(@Nullable Object object);
    }
}
