package com.example.offlinefirst.di;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.offlinefirst.db.MessageDao;
import com.example.offlinefirst.domain.repository.BaseMessageRepository;
import com.example.offlinefirst.domain.repository.LocalMessageRepository;
import com.example.offlinefirst.domain.repository.RemoteMessageRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = RoomModule.class)
public class RepositoryModule {

    @Singleton
    @Provides
    LocalMessageRepository provideLocalMessageRepository(
            MessageDao messageDao,
            SharedPreferences sharedPreferences) {
        return new LocalMessageRepository(messageDao, sharedPreferences);
    }

    @Singleton
    @Provides
    RemoteMessageRepository provideRemoteMessageRepository(Application application, FirebaseFirestore firestore) {
        return new RemoteMessageRepository(application.getApplicationContext(), firestore);
    }

    @Singleton
    @Provides
    BaseMessageRepository provideBaseMessageRepository(LocalMessageRepository localMessageRepository,
                                                       RemoteMessageRepository remoteMessageRepository) {
        return new BaseMessageRepository(localMessageRepository, remoteMessageRepository);
    }
}
