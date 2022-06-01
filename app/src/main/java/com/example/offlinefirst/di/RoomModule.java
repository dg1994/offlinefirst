package com.example.offlinefirst.di;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;

import com.example.offlinefirst.db.MessageDao;
import com.example.offlinefirst.db.OfflineMessageDatabase;
import com.example.offlinefirst.db.auth.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    @Singleton
    @Provides
    static OfflineMessageDatabase providesRoomDatabase(Application app) {
        return Room.databaseBuilder(app, OfflineMessageDatabase.class, "offline_message_database")
                .addMigrations(migrations)
                .build();
    }

    public static Migration[] migrations = {
    };

    @Singleton
    @Provides
    static MessageDao provideMessageDao(OfflineMessageDatabase db) {
        return db.messageDao();
    }

    @Singleton
    @Provides
    static UserDao provideUserDao(OfflineMessageDatabase db) {
        return db.userDao();
    }
}
