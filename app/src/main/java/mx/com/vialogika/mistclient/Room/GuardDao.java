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
    int saveGuard(Guard guard);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int[] saveGuards(List<Guard> guards);

    @Update
    void updateGuard(Guard guard);

    @Delete
    void deleteGuard(Guard guard);

    @Query("SELECT * FROM Guards WHERE localId = :id")
    Guard getGuard(int id);

}
