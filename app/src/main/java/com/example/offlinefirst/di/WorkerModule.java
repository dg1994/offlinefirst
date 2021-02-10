package com.example.offlinefirst.di;

import com.example.offlinefirst.workmanager.ChildWorkerFactory;
import com.example.offlinefirst.workmanager.CommentSaveWorker;
import com.example.offlinefirst.workmanager.CommentSyncWorker;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(CommentSyncWorker.class)
    public abstract ChildWorkerFactory bindSyncWorkerFactory(CommentSyncWorker.Factory worker);

    @Binds
    @IntoMap
    @WorkerKey(CommentSaveWorker.class)
    public abstract ChildWorkerFactory bindSaveWorkerFactory(CommentSaveWorker.Factory worker);
}
