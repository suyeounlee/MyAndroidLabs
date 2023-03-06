package algonquin.cst2335.lee00823;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import data.ChatMessage;

@Dao //Data Access Object
public interface ChatMessageDAO {

    @Insert
    void insertMessage(ChatMessage m);

    @Query("Select * from ChatMessage")
      List<ChatMessage> getAllMessages();

    @Delete
     void deleteMessage(ChatMessage m);




}
