package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;

@Dao
public interface ApostamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int saveApostamiento(Apostamiento apostamiento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int[] saveApostamientos(List<Apostamiento> apostamientos);
}
