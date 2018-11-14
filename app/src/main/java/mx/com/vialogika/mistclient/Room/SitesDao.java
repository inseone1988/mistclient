package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SitesDao {

    @Insert
    long saveSite(Site site);

    @Insert
    void saveSites(List<Site> sites);

    @Query("SELECT * FROM sites")
    List<Site> getSitenames();
}
