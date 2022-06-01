package com.example.offlinefirst.di;

import com.example.offlinefirst.workmanager.ChildWorkerFactory;
import com.example.offlinefirst.workmanager.MessageSaveWorker;
import com.example.offlinefirst.workmanager.MessageSyncWorker;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(MessageSyncWorker.class)
    public abstract ChildWorkerFactory bindSyncWorkerFactory(MessageSyncWorker.Factory worker);

    @Binds
    @IntoMap
    @WorkerKey(MessageSaveWorker.class)
    public abstract ChildWorkerFactory bindSaveWorkerFactory(MessageSaveWorker.Factory worker);
}
