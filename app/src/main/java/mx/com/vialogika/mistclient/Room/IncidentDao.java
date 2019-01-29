package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import mx.com.vialogika.mistclient.Incident;

@Dao
public interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveIncident(Incident incident);

}
