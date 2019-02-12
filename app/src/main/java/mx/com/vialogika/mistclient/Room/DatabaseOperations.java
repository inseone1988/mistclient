package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.Guard;
import mx.com.vialogika.mistclient.GuardForceState;
import mx.com.vialogika.mistclient.Incident;
import mx.com.vialogika.mistclient.Notif.AppNotifications;
import mx.com.vialogika.mistclient.Reporte;
import mx.com.vialogika.mistclient.User;
import mx.com.vialogika.mistclient.Utils.APDetailsDataHolder;
import mx.com.vialogika.mistclient.Utils.DatabaseOperationCallback;
import mx.com.vialogika.mistclient.Utils.Depuracion;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;
import mx.com.vialogika.mistclient.Utils.Provider;

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

    public void emptySitesTable(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.sitesDao().emptySitesTable();
                cb.onOperationSucceded("");
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

    public void getSiteNames(final simpleOperationCallback cb,final UIThreadOperation uicb){
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
               final List<Site> sites = appDatabase.sitesDao().getSitenames();
                cb.onOperationFinished(sites);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (uicb != null){
                            uicb.onOperationFinished(sites);
                        }
                    }
                });
            }
        }).start();

    }

    public void getSite(final int id, final simpleOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Site site = appDatabase.sitesDao().getSite(id);
                cb.onOperationFinished(site);
            }
        }).start();
    }

    public void updateSite(final Site site){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.sitesDao().updateSite(site);
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
        final Handler handler = new Handler(Looper.getMainLooper());
        final List<Reporte> fetched = new ArrayList<>();
        getLastReportId(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(Object response) {
                int lastid = (int) response;
                int userid = User.userId(ctx);
                String[] usersite = User.getManagedSites(ctx);
                NetworkRequest.fetchIncidents(ctx,"FETCH" ,lastid, userid, usersite, new NetworkRequestCallbacks() {
                    @Override
                    public void onNetworkRequestResponse(Object response) {
                        try{
                            JSONObject resp = new JSONObject(response.toString());
                            if (resp.length() > 0){
                                if (resp.getBoolean("success")){
                                    JSONArray reports = resp.getJSONArray("reports");
                                    if(reports.length() > 0){
                                        for (int i = 0;i < reports.length();i++){
                                            Reporte report = new Reporte(reports.getJSONObject(i));
                                            saveReport(report);
                                            fetched.add(report);
                                        }
                                    }
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final List<Reporte> reportes = appDatabase.reportDao().getAllReports();
                                            //Callback
                                            cb.onReportSynced(fetched);
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    cb.UIThreadOperation(reportes);
                                                }
                                            });
                                        }
                                    }).start();
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
                NetworkRequest.fetchIncidents(ctx, "UPDATE", lastid, userid, usersite, new NetworkRequestCallbacks() {
                    @Override
                    public void onNetworkRequestResponse(final Object response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject resp = new JSONObject(response.toString());
                                    if (resp.length() > 0){
                                        if (resp.getBoolean("success")){
                                            final JSONArray reports = resp.getJSONArray("reports");
                                            if(reports.length() > 0){
                                                for (int i = 0;i < reports.length();i++){
                                                    final JSONObject cReport = reports.getJSONObject(i);
                                                    Reporte old = appDatabase.reportDao().getReport(cReport.getInt("id"));
                                                    old.updateReportData(cReport);
                                                    appDatabase.reportDao().saveReport(old);
                                                }
                                            }
                                        }
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
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

    public void getAllApostamientos(final doInBackgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Apostamiento> apts = appDatabase.apostamientoDao().getAllApostamientos();
                cb.onOperationFinished(apts);
            }
        }).start();
    }

    public void getAllClients(final doInBackgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Client> clientes = appDatabase.clientDao().getAllClientes();
                cb.onOperationFinished(clientes);
            }
        }).start();
    }

    public void getAllSites(final doInBackgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Site> sites = appDatabase.sitesDao().getSitenames();
                cb.onOperationFinished(sites);
            }
        }).start();
    }

    public void saveApostamiento(final Apostamiento ap,final doInBackgroundOperation cb){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                long saved = appDatabase.apostamientoDao().saveApostamiento(ap);
                final Apostamiento apt = appDatabase.apostamientoDao().getApostamiento((int)saved);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.onOperationFinished(apt);
                    }
                });
            }
        }).start();
    }

    public void getApostamientos(final int siteid, final doInBackgroundOperation bgo, final UIThreadOperation uicb){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Apostamiento> apts = appDatabase.apostamientoDao().getApostamientosBySiteId(siteid);
                bgo.onOperationFinished(apts);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       uicb.onOperationFinished(bgo);
                    }
                });
            }
        }).start();
    }

    public void deleteApostamiento(final int apid, final doInBackgroundOperation cb, final UIThreadOperation uiThreadOperation){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int affected = appDatabase.apostamientoDao().deleteApostamiento(apid);
                cb.onOperationFinished(affected);
                if (affected != 0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            uiThreadOperation.onOperationFinished(affected);
                        }
                    });
                }
            }
        }).start();
    }

    public void updateReport(Reporte report){
        appDatabase.reportDao().updateReport(report);
    }

    public void getClientsBySiteId(final int siteId,final doInBackgroundOperation dbcb,final UIThreadOperation uicb){
        final Handler handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Client> clients = appDatabase.clientDao().getClientsBySiteId(siteId);
                dbcb.onOperationFinished(clients);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uicb.onOperationFinished(clients);
                    }
                });
            }
        }).start();

    }

    public void getReport(final int uid,final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
            Reporte mReport = appDatabase.reportDao().getReport(uid);
            cb.onOperationSucceded(mReport);
            }
        }).start();

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
        if (appDatabase.isOpen()){
            appDatabase.close();
        }
    }

    public void archiveReport(final int reportId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.reportDao().setReportArchived(reportId);
            }
        }).start();
    }

    public void activeReport(final int reportId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.reportDao().setReportActive(reportId);
            }
        }).start();
    }

    public void getArchivedReports(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Reporte> archived = appDatabase.reportDao().getArchivedReports();
                cb.onOperationSucceded(archived);
            }
        }).start();
    }

    public void getGuard(final int personid, final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Guard guard = appDatabase.guardDao().getGuardByPersonId(personid);
                cb.onOperationSucceded(guard);
            }
        }).start();
    }

    public void saveGuard(final Guard guard){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.guardDao().saveGuard(guard);
            }
        }).start();
    }

    public void emptyGuardsTable(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 int affected = appDatabase.guardDao().emptyGuards();
                cb.onOperationSucceded(affected);
            }
        }).start();
    }

    public void syncEdoData(JSONObject response,boolean downloadProfilePhotos){
            List<Guard> guards = new ArrayList<>();
            List<Client> clients = new ArrayList<>();
            List<Apostamiento> apostamientos = new ArrayList<>();
            try{
                final JSONArray g = response.getJSONArray("guards");
                JSONArray c = response.getJSONArray("clients");
                JSONArray p = response.getJSONArray("places");
                RequestQueue rq = Volley.newRequestQueue(ctx);
                final int NOTIF_ID = 1234;
                final NotificationManagerCompat nManager = NotificationManagerCompat.from(ctx.getApplicationContext());
                AppNotifications notif = new AppNotifications(ctx.getApplicationContext());
                notif.setPROGRESS_MAX(g.length());
                final NotificationCompat.Builder dNotif = notif.downloadNotification();
                for (int i = 0;i < g.length();i++){
                    final Guard guard = new Guard(g.getJSONObject(i));
                    final int counter = i + 1;
                    if (downloadProfilePhotos){
                        nManager.notify(NOTIF_ID, dNotif.build());
                        if (guard.hasProfilePhoto()){
                            ImageRequest ir = NetworkRequest.getProfileImage(ctx, guard.getPersonProfilePhotoPath(), new NetworkRequestCallbacks() {
                                @Override
                                public void onNetworkRequestResponse(Object response) {
                                    guard.setLocalPhotoPath((String) response);
                                    saveGuard(guard);
                                    dNotif.setProgress(g.length(),counter,false);
                                    nManager.notify(NOTIF_ID,dNotif.build());
                                }
                                @Override
                                public void onNetworkRequestError(VolleyError error) {

                                }
                            });
                            rq.add(ir);
                        }else{
                            saveGuard(guard);
                            dNotif.setProgress(g.length(),counter,false);
                            nManager.notify(NOTIF_ID,dNotif.build());
                        }
                    }else{
                        saveGuard(guard);
                    }
                }
                dNotif.setContentTitle("Imagenes descargadas");
                nManager.notify(NOTIF_ID,dNotif.build());
                //syncGuards(guards);
                for (int i = 0; i < c.length();i++){
                    clients.add(new Client(c.getJSONObject(i)));
                }
                for (int i = 0; i < p.length();i++){
                    apostamientos.add(new Apostamiento(p.getJSONObject(i)));
                }
                syncClients(clients);
                syncApostamientos(apostamientos);
            }catch(JSONException e){
                e.printStackTrace();
            }
    }

    public static int counter(int position){
        return position +1;
    }

    public void syncGuards(final List<Guard> guards){
        emptyGuardsTable(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                saveGuards(guards, new DatabaseOperationCallback() {
                    @Override
                    public void onOperationSucceded(@Nullable Object response) {
                        long[] affected = (long[]) response;
                        Log.d(Depuracion.DEBUG_ROOM_MESSAGE,"Saved guards " + affected.length);
                    }
                });
            }
        });
    }

    public void syncClients(final List<Client> clients){
        emptyClientsTable(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                saveClients(clients, new DatabaseOperationCallback() {
                    @Override
                    public void onOperationSucceded(@Nullable Object response) {
                        long[] affected = (long[]) response;
                        Log.d(Depuracion.DEBUG_ROOM_MESSAGE,"Saved clients " + affected.length);
                    }
                });
            }
        });
    }

    public void syncApostamientos(final List<Apostamiento> apostamientos){
        emptyApostamientosTable(new DatabaseOperationCallback() {
            @Override
            public void onOperationSucceded(@Nullable Object response) {
                saveApostamientos(apostamientos, new DatabaseOperationCallback() {
                    @Override
                    public void onOperationSucceded(@Nullable Object response) {
                        long[] affectde = (long[]) response;
                        Log.d(Depuracion.DEBUG_ROOM_MESSAGE,"Saved apostamientos " + affectde.length);
                    }
                });

            }
        });
    }


    public void saveApostamientos(final List<Apostamiento> apostamientos,final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long[] affected = appDatabase.apostamientoDao().saveApostamientos(apostamientos);
                cb.onOperationSucceded(affected);
            }
        }).start();
    }

    public void emptyApostamientosTable(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int affected = appDatabase.apostamientoDao().emptyApostamientosTable();
                cb.onOperationSucceded(affected);
            }
        }).start();
    }

    public void emptyClientsTable(final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int affected = appDatabase.clientDao().emptyClientsTable();
                cb.onOperationSucceded(affected);
            }
        }).start();
    }

    public void saveGuards(final List<Guard> guards,final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long[] saved = appDatabase.guardDao().saveGuards(guards);
                cb.onOperationSucceded(saved);
            }
        }).start();
    }

    public void saveClients(final List<Client> clients, final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long[] saved = appDatabase.clientDao().saveClients(clients);
                cb.onOperationSucceded(saved);
            }
        }).start();
    }

    public void savePlaces(final List<Apostamiento> apostamientos,final DatabaseOperationCallback  cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long[] saved = appDatabase.apostamientoDao().saveApostamientos(apostamientos);
                cb.onOperationSucceded(saved);
            }
        }).start();
    }

    public int[] repToUpdate(){
        return appDatabase.reportDao().getIndexToUpdate();
    }

    public void getGuardsFromSite(final int siteid, final DatabaseOperationCallback cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Guard> guards = appDatabase.guardDao().getGuardsFromSite(siteid);
                cb.onOperationSucceded(guards);
            }
        }).start();
    }

    public void saveProvider(final Provider provider, final doInBackgroundOperation dbcb, final UIThreadOperation uicb){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long saved = appDatabase.providerDao().saveProvider(provider);
                dbcb.onOperationFinished(saved);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uicb.onOperationFinished(saved);
                    }
                });
            }
        }).start();
    }

    public void loadProviders(final doInBackgroundOperation bo,final UIThreadOperation uiop){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Provider> providers = appDatabase.providerDao().getProviders();
                bo.onOperationFinished(providers);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiop.onOperationFinished(providers);
                    }
                });
            }
        }).start();
    }

    public void getEdoReports(final int siteid,final String from,final String to,final doInBackgroundOperation dbo,final UIThreadOperation uiop){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<GuardForceState> list = appDatabase.guardEdoReportDao().stateList(siteid,from,to);
                if(list.size() > 0){
                    for (int i = 0; i < list.size(); i++) {
                        GuardForceState item = list.get(i);
                        Guard guardinfo = appDatabase.guardDao().getGuardByHash(item.getEdoFuerzaGuardId());
                        if (guardinfo != null){
                            Apostamiento apostamiento = appDatabase.apostamientoDao().getApostamientobyId(Integer.valueOf(item.getEdoFuerzaPlaceId()));
                            item.setGuardName(guardinfo.getGuardFullname());
                            item.setGuardPhotoPath(guardinfo.getLocalPhotoPath());
                            item.setApName(apostamiento.getPlantillaPlaceApostamientoAlias());
                        }
                    }
                }
                dbo.onOperationFinished(list);
                //TODO:Main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiop.onOperationFinished(list);
                    }
                });
            }
        }).start();
    }

    public void getGuardProfilePhotoPath(final int guardId, final doInBackgroundOperation dbo, final UIThreadOperation uiThreadOperation){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String path = appDatabase.guardDao().getGuardProfilePhotoPath(guardId);
                dbo.onOperationFinished(path);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiThreadOperation.onOperationFinished(path);
                    }
                });
            }
        }).start();
    }
    public void checkDatabase(final doInBackgroundOperation cb,final UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean checkPassed = false;
                int guardCount = appDatabase.guardDao().guardsCount();
                int providerCount = appDatabase.providerDao().countProviders();
                int clientCount = appDatabase.clientDao().clientCount();
                if (guardCount != 0 && providerCount != 0 && clientCount != 0){
                    checkPassed = true;
                }
                cb.onOperationFinished(checkPassed);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uito.onOperationFinished(null);
                    }
                });
            }
        }).start();
    }

    public void getProviders(final doInBackgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Provider> allProviders = appDatabase.providerDao().getProviders();
                cb.onOperationFinished(allProviders);
            }
        }).start();
    }

    public void syncEdoReports(final List<GuardForceState> stateList, final doInBackgroundOperation bo, final UIThreadOperation uiThreadOperation){
        //Nunca se debe usar estos datos para vista porque estan incompletos solo para sincronizar
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<GuardForceState> alreadySaved = new ArrayList<>();
                for (int i = 0; i < stateList.size();i++){
                    GuardForceState item = stateList.get(i);
                    long saved = appDatabase.guardEdoReportDao().alreadySaved(item.getId());
                    if (saved == 0 ){
                        long id = appDatabase.guardEdoReportDao().saveGRDReport(item);
                        if (id != 0){
                            alreadySaved.add(item);
                        }
                    }
                }
                bo.onOperationFinished(alreadySaved);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiThreadOperation.onOperationFinished(alreadySaved);
                    }
                });
            }
        }).start();
    }

    public void getAPCoveredDetails(final String from,final String to,final String group,final int siteid,final doInBackgroundOperation dibo,final @Nullable UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<APDetailsDataHolder> holders = new ArrayList<>();
                List<Apostamiento> apostamientos = appDatabase.apostamientoDao().getApostamientosBySiteId(siteid);
                for (int i = 0; i < apostamientos.size(); i++) {
                    Apostamiento current = apostamientos.get(i);
                    int required = appDatabase.apostamientoDao().guardsRequiredByApostamiento(current.getPlantillaPlaceId());
                    int reported = appDatabase.guardEdoReportDao().getCountByAP(from,to,group,current.getPlantillaPlaceId());
                    holders.add(new APDetailsDataHolder(current.getPlantillaPlaceApostamientoName(),required,reported));
                }
                dibo.onOperationFinished(holders);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uito.onOperationFinished(holders);
                    }
                });
            }
        }).start();
    }

    public void getGroupNames(final String from,final String to,final doInBackgroundOperation dib,final UIThreadOperation uiThreadOperation){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] grupos = appDatabase.guardEdoReportDao().getGroups(from,to);
                dib.onOperationFinished(grupos);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiThreadOperation.onOperationFinished(grupos);
                    }
                });
            }
        }).start();
    }

    public void saveClient(final Client client,final doInBackgroundOperation dib,final UIThreadOperation uiThreadOperation){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long saved = appDatabase.clientDao().saveClient(client);
                final Client updated = appDatabase.clientDao().getClientById((int)saved);
                dib.onOperationFinished(saved);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiThreadOperation.onOperationFinished(updated);
                    }
                });
            }
        }).start();
    }

    public void deleteAllTables(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.clearAllTables();
            }
        }).start();
    }

    public void deleteClient(final int id,final doInBackgroundOperation dibo,final UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable(){
            @Override
            public void run(){
                //TODO: Make background operations
                final int deleted = appDatabase.clientDao().deleteClient(id);
                //TODO: Execute background callback - Add object response
                dibo.onOperationFinished(deleted);
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        //TODO:Execute main UIThread callback
                        uito.onOperationFinished(deleted);
                    }
                });

            }
        }).start();
    }

    public void saveIncident(final Incident incident, final doInBackgroundOperation db, final UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long saved = appDatabase.incidentDao().saveIncident(incident);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uito.onOperationFinished(saved);
                    }
                });
            }
        }).start();
    }

    public void getPendingIncidents(final doInBackgroundOperation cb,final UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Incident> pending = appDatabase.incidentDao().getPendingIncidents();
                cb.onOperationFinished(pending);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uito.onOperationFinished(pending);
                    }
                });
            }
        }).start();

    }

    public void deleteIncident(final int incodentid, final doInBackgroundOperation cb){
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int deleted = appDatabase.incidentDao().deleteIncident(incodentid);
                cb.onOperationFinished(deleted);
            }
        }).start();

    }


    public void getSiteGuardsRequired(final int siteid, final doInBackgroundOperation cb, final UIThreadOperation uito){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int req = appDatabase.apostamientoDao().guardsRequired(siteid);
                cb.onOperationFinished(req);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uito.onOperationFinished(req);
                    }
                });
            }
        }).start();
    }

    public interface Sync{
        void onReportSynced(List<Reporte> reports);
        void UIThreadOperation(Object response);
    }

    public interface simpleOperationCallback{
        //Generic callback that accpts a result from background operation or generic type return
        void onOperationFinished(@Nullable Object object);
    }

    public interface doInBackgroundOperation{
        void onOperationFinished(@Nullable Object object);
    }

    public interface UIThreadOperation{
        void onOperationFinished(@Nullable Object object);
    }
}
