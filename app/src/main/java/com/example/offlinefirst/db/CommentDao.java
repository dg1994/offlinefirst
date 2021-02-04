package com.example.offlinefirst.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.offlinefirst.model.Comment;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM comment WHERE chat_id = :chatId ORDER BY timestamp DESC limit :requestLoadSize")
    List<Comment> getComments(long chatId, int requestLoadSize);

    @Query("SELECT * FROM comment WHERE chat_id = :chatId AND timestamp < :key ORDER BY timestamp DESC limit :requestLoadSize")
    List<Comment> getCommentsAfter(long chatId, long key, int requestLoadSize);

    @Query("SELECT * FROM comment WHERE id = :commentId")
    Comment getComment(long commentId);

    @Query("SELECT * FROM comment WHERE chat_id = :chatId ORDER BY timestamp DESC")
    DataSource.Factory<Integer, Comment> allComments(long chatId);
}
