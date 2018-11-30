package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;

@Dao
public interface ApostamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveApostamiento(Apostamiento apostamiento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveApostamientos(List<Apostamiento> apostamientos);

    @Query("DELETE FROM Apostamientos")
    int emptyApostamientosTable();

    @Query("SELECT plantillaPlaceSiteId,localId,plantillaPlaceId,plantillaPlaceClientId,plantillaPlaceApostamientoName,plantillaPlaceApostamientoAlias,plantillaPlaceType,(SELECT Clients.clientAlias FROM Clients WHERE clientId = Apostamientos.plantillaPlaceClientId) AS clientName FROM Apostamientos WHERE plantillaPlaceSiteId = :id")
    List<Apostamiento> getApostamientosBySiteId(int id);
}
