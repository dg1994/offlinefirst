package com.example.offlinefirst.di;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;

import com.example.offlinefirst.db.CommentDao;
import com.example.offlinefirst.db.OfflineCommentDatabase;
import com.example.offlinefirst.db.auth.AccountPropertiesDao;
import com.example.offlinefirst.db.auth.AuthTokenDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    @Singleton
    @Provides
    static OfflineCommentDatabase providesRoomDatabase(Application app) {
        return Room.databaseBuilder(app, OfflineCommentDatabase.class, "offline_comment_database")
                .addMigrations(migrations)
                .build();
    }

    public static Migration[] migrations = {
    };

    @Singleton
    @Provides
    static CommentDao provideCommentDao(OfflineCommentDatabase db) {
        return db.commentDao();
    }

    @Singleton
    @Provides
    static AuthTokenDao provideAuthTokenDao(OfflineCommentDatabase db) {
        return db.authTokenDao();
    }

    @Singleton
    @Provides
    static AccountPropertiesDao provideAccountPropertiesDao(OfflineCommentDatabase db) {
        return db.accountPropertiesDao();
    }
}
