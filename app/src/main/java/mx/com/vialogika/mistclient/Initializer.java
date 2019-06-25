package mx.com.vialogika.mistclient;

import android.app.Application;
import android.content.Context;

import net.gotev.uploadservice.UploadService;

public class Initializer extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return context;
    }
}
