package com.example.offlinefirst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.offlinefirst.db.auth.AccountPropertiesDao;
import com.example.offlinefirst.db.auth.AuthTokenDao;
import com.example.offlinefirst.model.AccountProperties;
import com.example.offlinefirst.model.AuthToken;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Constants;

@Database(entities = {Comment.class, AuthToken.class, AccountProperties.class}, version = Constants.DB_VERSION)
public abstract class OfflineCommentDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
    public abstract AuthTokenDao authTokenDao();
    public abstract AccountPropertiesDao accountPropertiesDao();
}