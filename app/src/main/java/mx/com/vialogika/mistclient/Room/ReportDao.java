package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.com.vialogika.mistclient.Reporte;

@Dao
public interface ReportDao {
    @Insert
    void saveReport(Reporte reporte);

    @Insert
    void saveReports(List<Reporte> reports);

    @Query("SELECT remReportId FROM Reports ORDER BY remReportId DESC LIMIT 1")
    int  fetchLastReportID ();

    @Query("SELECT * FROM Reports")
    List<Reporte> getAllReports();

    @Query("DELETE FROM Reports")
    void eraseReportTable();

}
