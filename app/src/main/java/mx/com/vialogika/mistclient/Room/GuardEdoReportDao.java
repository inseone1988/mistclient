package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.com.vialogika.mistclient.GuardForceState;

@Dao
public interface GuardEdoReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveGRDReport(GuardForceState grd);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveGRDReports(List<GuardForceState> states);

    @Query("SELECT id FROM GuardsForceState WHERE id = :id")
    long alreadySaved(int id);

    @Query("SELECT * FROM GuardsForceState WHERE edoFuerzaSiteId = :siteid AND edoFuerzaReported BETWEEN :from AND :to ORDER BY edoFuerzaDate ASC")
    List<GuardForceState> stateList(int siteid,String from,String to);

    @Query("SELECT COUNT(id) FROM GuardsForceState WHERE edoFuerzaPlaceId = :apid")
    int getCountByAP(int apid);

    @Query("SELECT COUNT(id) FROM GuardsForceState WHERE edoFuerzaPlaceId = :apid AND edoFuerzaTurno = :group AND edoFuerzaDate BETWEEN :from AND :to")
    int getCountByAP(String from,String to,String group,int apid);

    @Query("SELECT edoFuerzaTurno FROM GuardsForceState WHERE edoFuerzaDate BETWEEN :from AND :to")
    String[] getGroups(String from,String to);

}
