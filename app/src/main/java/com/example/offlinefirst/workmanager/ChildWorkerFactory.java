package com.example.offlinefirst.workmanager;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public interface ChildWorkerFactory {
    Worker create(Context appContext, WorkerParameters params);
}
