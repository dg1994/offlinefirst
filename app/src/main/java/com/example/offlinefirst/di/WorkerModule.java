package com.example.offlinefirst.di;

import com.example.offlinefirst.workmanager.ChildWorkerFactory;
import com.example.offlinefirst.workmanager.CommentSyncWorker;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(CommentSyncWorker.class)
    public abstract ChildWorkerFactory bindWorkerFactory(CommentSyncWorker.Factory worker);
}
