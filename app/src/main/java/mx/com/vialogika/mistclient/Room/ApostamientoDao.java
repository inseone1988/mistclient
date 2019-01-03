package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Utils.APDetailsDataHolder;

@Dao
public interface ApostamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveApostamiento(Apostamiento apostamiento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveApostamientos(List<Apostamiento> apostamientos);

    @Query("DELETE FROM Apostamientos")
    int emptyApostamientosTable();

    @Query("DELETE FROM apostamientos WHERE plantillaPlaceId = :id")
    int deleteApostamiento(int id);

    @Query("SELECT plantillaPlaceSiteId,localId,plantillaPlaceId,plantillaPlaceClientId,plantillaPlaceApostamientoName,plantillaPlaceApostamientoAlias,plantillaPlaceType,(SELECT Clients.clientAlias FROM Clients WHERE clientId = Apostamientos.plantillaPlaceClientId) AS clientName,plantillaPlaceGuardsRequired,plantillaPlaceStatus FROM Apostamientos WHERE plantillaPlaceSiteId = :id ORDER BY plantillaPlaceApostamientoAlias ASC")
    List<Apostamiento> getApostamientosBySiteId(int id);

    @Query("SELECT * FROM Apostamientos WHERE plantillaPlaceId = :id")
    Apostamiento getApostamientobyId(int id);

    @Query("SELECT * FROM Apostamientos WHERE localId = :id")
    Apostamiento getApostamiento(int id);

    @Query("SELECT plantillaPlaceGuardsRequired FROM Apostamientos WHERE plantillaPlaceId = :placeId")
    int guardsRequiredByApostamiento(int placeId);

    @Query("SELECT SUM(plantillaPlaceGuardsRequired) FROM Apostamientos WHERE plantillaPlaceSiteId = :siteid")
    int guardsRequired(int siteid);

    @Query("SELECT * FROM Apostamientos ORDER BY plantillaPlaceApostamientoAlias ASC")
    List<Apostamiento> getAllApostamientos();

    @Query("SELECT COUNT(localId) FROM Apostamientos")
    int ApostamientosCount();
}
