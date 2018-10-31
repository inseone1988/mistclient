package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Incidents {

    @PrimaryKey(autoGenerate = true)
    private int id;
}
