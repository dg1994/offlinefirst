package com.example.offlinefirst.domain.repository;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Constants;
import com.example.offlinefirst.workmanager.CommentSyncWorker;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

public class RemoteCommentRepository {

    private Context context;

    public RemoteCommentRepository(Context applicationContext) {
        context = applicationContext;
    }

    public Completable sync(Comment comment) {
        return Completable.fromAction(() -> {
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            Data data = new Data.Builder()
                    .putLong(Constants.KEY_COMMENT_ID, comment.getId())
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CommentSyncWorker.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .addTag("comment-sync")
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                    .build();

            WorkManager.getInstance(context)
                    .beginUniqueWork(CommentSyncWorker.TAG + comment.getId(), ExistingWorkPolicy.REPLACE, request)
                    .enqueue();
        });
    }

    public Completable stopSync(Comment comment) {
        return Completable.fromAction(() -> WorkManager.getInstance(context)
                .cancelUniqueWork(CommentSyncWorker.TAG + comment.getId()));
    }
}
