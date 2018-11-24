package mx.com.vialogika.mistclient;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "EdoReports")
public class EdoReport {

    @PrimaryKey(autoGenerate = true)
    private int localId;
}
