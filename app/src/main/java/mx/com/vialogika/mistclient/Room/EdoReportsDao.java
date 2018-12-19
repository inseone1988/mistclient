package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import java.util.List;

import mx.com.vialogika.mistclient.EdoReport;

@Dao
public interface EdoReportsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveEdoReport(EdoReport edoReport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveEdoReports(List<EdoReport> edoReports);
}
