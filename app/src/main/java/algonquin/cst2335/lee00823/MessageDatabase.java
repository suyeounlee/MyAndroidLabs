package algonquin.cst2335.lee00823;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import data.ChatMessage;
//this Database class is meant for storing ChatMessage objects, and uses the ChatMessageDAO class for querying data.

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract ChatMessageDAO cmDAO();

}
