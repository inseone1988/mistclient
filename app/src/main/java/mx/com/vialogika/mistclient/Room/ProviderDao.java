package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Utils.Provider;

@Dao
public interface ProviderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveProvider(Provider provider);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveProviders(List<Provider> providers);

    @Update
    int updateProvider(Provider provider);

    @Query("SELECT * FROM Providers")
    List<Provider> getProviders();

    @Query("SELECT * FROM Providers WHERE providerId = :id")
    Provider getProviderById(int id);

    @Query("SELECT COUNT(localId) FROM Providers")
    int countProviders();

}
