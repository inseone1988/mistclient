package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Reporte;

@Dao
public interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveReport(Reporte reporte);

    @Insert
    void saveReports(List<Reporte> reports);

    @Query("SELECT remReportId FROM Reports ORDER BY remReportId DESC LIMIT 1")
    int  fetchLastReportID ();

    @Query("SELECT * FROM Reports WHERE reportStatus != 'Archived' ORDER BY reportTimeStamp DESC")
    List<Reporte> getAllReports();

    @Query("DELETE FROM Reports")
    void eraseReportTable();

    @Query("UPDATE Reports SET reportStatus = 'Archived' WHERE remReportId = :id")
    void setReportArchived(int id);

    @Query("SELECT * FROM Reports WHERE reportStatus = 'Archived'")
    List<Reporte> getArchivedReports();

    @Query("UPDATE Reports SET reportStatus = 'Active' WHERE remReportId = :id")
    void setReportActive(int id);

    @Query("SELECT remReportId FROM Reports")
    int[] getIndexToUpdate();
    @Query("SELECT * FROM Reports WHERE remReportId = :repid")
    Reporte getReport(int repid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReport(Reporte reporte);

}
