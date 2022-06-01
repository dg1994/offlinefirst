package com.example.offlinefirst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.offlinefirst.db.auth.UserDao;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.model.User;
import com.example.offlinefirst.utils.Constants;

@Database(entities = {Message.class, User.class}, version = Constants.DB_VERSION)
public abstract class OfflineMessageDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
    public abstract UserDao userDao();
}