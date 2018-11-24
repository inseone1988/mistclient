package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.Reporte;

@Database(entities = {Reporte.class,Comment.class,Site.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract CommentDao commentDao();
    public abstract SitesDao sitesDao();
    public abstract GuardDao guardDao();
    public abstract ApostamientoDao apostamientoDao();
}
