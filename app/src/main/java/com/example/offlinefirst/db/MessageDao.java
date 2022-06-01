package com.example.offlinefirst.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.offlinefirst.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> addAll(List<Message> messages);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM Message WHERE chat_id = :chatId ORDER BY timestamp DESC limit :requestLoadSize")
    List<Message> getMessages(long chatId, int requestLoadSize);

    @Query("SELECT * FROM Message WHERE chat_id = :chatId AND timestamp < :key ORDER BY timestamp DESC limit :requestLoadSize")
    List<Message> getMessagesAfter(long chatId, long key, int requestLoadSize);

    @Query("SELECT * FROM Message WHERE id = :commentId")
    Message getMessage(String commentId);

    @Query("SELECT * FROM Message WHERE chat_id = :chatId ORDER BY timestamp DESC")
    DataSource.Factory<Integer, Message> allMessages(long chatId);
}
