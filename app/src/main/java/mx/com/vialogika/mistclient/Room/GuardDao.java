package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Guard;

@Dao
public interface GuardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveGuard(Guard guard);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveGuards(List<Guard> guards);

    @Update
    void updateGuard(Guard guard);

    @Delete
    void deleteGuard(Guard guard);

    @Query("SELECT * FROM Guards WHERE guardId = :id")
    Guard getGuard(int id);

     @Query("SELECT * FROM Guards WHERE guardPersonId = :id")
    Guard getGuardByPersonId(int id);

     @Query("DELETE FROM Guards")
    int emptyGuards();

     @Query("SELECT * FROM Guards WHERE guardSite = :id AND guardStatus = 1 ORDER BY personName")
    List<Guard> getGuardsFromSite(int id);

     @Query ("SELECT * FROM Guards WHERE guardHash = :hash")
    Guard getGuardByHash(String hash);

     @Query("SELECT COUNT(localId) FROM Guards")
    int guardsCount();

     @Query("SELECT localPhotoPath FROM guards WHERE guardId = :id")
    String getGuardProfilePhotoPath(int id);

}
