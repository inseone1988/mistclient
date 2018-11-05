package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mx.com.vialogika.mistclient.Reporte;

@Database(entities = {Reporte.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
}