package com.example.offlinefirst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Constants;

@Database(entities = {Comment.class}, version = Constants.DB_VERSION)
public abstract class OfflineCommentDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
}
