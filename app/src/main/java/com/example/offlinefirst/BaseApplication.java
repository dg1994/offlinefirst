package com.example.offlinefirst;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.example.offlinefirst.di.DaggerAppComponent;
import com.example.offlinefirst.workmanager.MyWorkerFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    @Inject
    MyWorkerFactory workerFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        WorkManager.initialize(this, new Configuration.Builder().setWorkerFactory(workerFactory).build());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
