package com.example.offlinefirst.di;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.offlinefirst.db.CommentDao;
import com.example.offlinefirst.domain.repository.BaseCommentRepository;
import com.example.offlinefirst.domain.repository.LocalCommentRepository;
import com.example.offlinefirst.domain.repository.RemoteCommentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = RoomModule.class)
public class RepositoryModule {

    @Singleton
    @Provides
    LocalCommentRepository provideLocalCommentRepository(
            CommentDao commentDao,
            SharedPreferences sharedPreferences) {
        return new LocalCommentRepository(commentDao,  sharedPreferences);
    }

    @Singleton
    @Provides
    RemoteCommentRepository provideRemoteCommentRepository(Application application, FirebaseFirestore firestore) {
        return new RemoteCommentRepository(application.getApplicationContext(), firestore);
    }

    @Singleton
    @Provides
    BaseCommentRepository provideBaseCommentRepository(LocalCommentRepository localCommentRepository,
                                                       RemoteCommentRepository remoteCommentRepository) {
        return new BaseCommentRepository(localCommentRepository, remoteCommentRepository);
    }
}
