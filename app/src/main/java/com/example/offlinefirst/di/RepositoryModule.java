package com.example.offlinefirst.di;

import android.app.Application;

import com.example.offlinefirst.db.CommentDao;
import com.example.offlinefirst.db.auth.AccountPropertiesDao;
import com.example.offlinefirst.db.auth.AuthTokenDao;
import com.example.offlinefirst.domain.repository.BaseCommentRepository;
import com.example.offlinefirst.domain.repository.LocalCommentRepository;
import com.example.offlinefirst.domain.repository.RemoteCommentRepository;
import com.example.offlinefirst.domain.repository.auth.AuthRepository;
import com.example.offlinefirst.network.auth.AuthApiService;
import com.example.offlinefirst.session.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = RoomModule.class)
public class RepositoryModule {

    @Singleton
    @Provides
    LocalCommentRepository provideLocalCommentRepository(CommentDao commentDao) {
        return new LocalCommentRepository(commentDao);
    }

    @Singleton
    @Provides
    RemoteCommentRepository provideRemoteCommentRepository(Application application) {
        return new RemoteCommentRepository(application.getApplicationContext());
    }

    @Singleton
    @Provides
    BaseCommentRepository provideBaseCommentRepository(LocalCommentRepository localCommentRepository,
                                                       RemoteCommentRepository remoteCommentRepository) {
        return new BaseCommentRepository(localCommentRepository, remoteCommentRepository);
    }
}
