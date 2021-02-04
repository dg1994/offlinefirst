package com.example.offlinefirst.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class MyWorkerFactory extends WorkerFactory {

    private final Map<Class<? extends Worker>, Provider<ChildWorkerFactory>> workerFactories;

    @Inject
    public MyWorkerFactory(Map<Class<? extends Worker>, Provider<ChildWorkerFactory>> workerFactories) {
        this.workerFactories = workerFactories;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext, @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {

        try {
            Provider<ChildWorkerFactory> creator = workerFactories.get(Class.forName(workerClassName));
            if (creator == null) { // if the workerFactory has not been created

                // loop through the allowable keys (aka allowed classes with the @WorkerKey)
                for (Map.Entry<Class<? extends Worker>, Provider<ChildWorkerFactory>> entry : workerFactories.entrySet()) {

                    // if it's allowed, set the Provider<ChildWorkerFactory>
                    if (Class.forName(workerClassName).isAssignableFrom(entry.getKey())) {
                        creator = entry.getValue();
                        break;
                    }
                }
            }

            // if this is not one of the allowed keys, throw exception
            if (creator == null) {
                throw new IllegalArgumentException("unknown model class " + Class.forName(workerClassName));
            }

            // return the Provider
            try {
                return creator.get().create(appContext, workerParameters);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
