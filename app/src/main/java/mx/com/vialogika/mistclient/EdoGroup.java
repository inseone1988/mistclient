package mx.com.vialogika.mistclient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Groups")
public class EdoGroup {

    @PrimaryKey(autoGenerate = true)
    private int localId;
}
