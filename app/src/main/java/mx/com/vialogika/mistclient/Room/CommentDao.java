package mx.com.vialogika.mistclient.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.com.vialogika.mistclient.Comment;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveComment(Comment... comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveComments(List<Comment> comments);

    @Query("SELECT * FROM Comments WHERE eventId = :eventid ORDER BY commentTimestamp DESC")
    List<Comment> getEventComments(int eventid);

    @Update
    void updateComment(Comment... comment);
}
