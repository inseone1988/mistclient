package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Incident;

@Dao
public interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveIncident(Incident incident);

    @Query("SELECT * FROM Incidents WHERE eventEditStatus = 1")
    List<Incident> getPendingIncidents();

    @Query("DELETE FROM incidents WHERE localId = :id")
    int deleteIncident(int id);

}
