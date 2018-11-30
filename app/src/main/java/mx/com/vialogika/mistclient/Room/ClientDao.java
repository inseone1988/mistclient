package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Client;

@Dao
public interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveClient(Client client);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] saveClients(List<Client> clients);

    @Update
    void updateCliente(Client cliente);

    @Query("DELETE FROM Clients")
    int emptyClientsTable();

    @Query("SELECT * FROM Clients WHERE clientSiteId = :id")
    List<Client> getClientsBySiteId(int id);
}
