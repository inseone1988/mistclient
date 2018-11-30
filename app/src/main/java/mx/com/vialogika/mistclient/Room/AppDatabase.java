package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import java.security.ProviderException;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.Guard;
import mx.com.vialogika.mistclient.Reporte;
import mx.com.vialogika.mistclient.Utils.Provider;

@Database(entities = {Reporte.class,Comment.class,Site.class,Guard.class,Apostamiento.class,Client.class,Provider.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract CommentDao commentDao();
    public abstract SitesDao sitesDao();
    public abstract GuardDao guardDao();
    public abstract ApostamientoDao apostamientoDao();
    public abstract ClientDao clientDao();
    public abstract ProviderDao providerDao();
}
