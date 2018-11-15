package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSite(Site site);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSites(List<Site> sites);

    @Query("SELECT * FROM Sites")
    List<Site> getSitenames();

    @Query("DELETE FROM Sites")
    void deleteSitesTable();
}
