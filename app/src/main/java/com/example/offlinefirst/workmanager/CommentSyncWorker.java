package com.example.offlinefirst.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.domain.repository.LocalCommentRepository;
import com.example.offlinefirst.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

public class CommentSyncWorker extends Worker {

    public static final String TAG = "CommentSyncWorker";
    private LocalCommentRepository localCommentRepository;
    private FirebaseFirestore firestore;

    public CommentSyncWorker(LocalCommentRepository localCommentRepository, FirebaseFirestore firestore,
                             Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        this.localCommentRepository = localCommentRepository;
        this.firestore = firestore;
    }

    @NonNull
    @Override
    public Result doWork() {

        String commentId = getInputData().getString(Constants.KEY_COMMENT_ID);
        Comment comment = localCommentRepository.getComment(commentId);

        final Result[] result = {Result.retry()};
        CountDownLatch countDownLatch = new CountDownLatch(1);
        if (!isStopped()) {
            DocumentReference newCommentRef = firestore.collection("chats")
                    .document();

            newCommentRef.set(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        result[0] = Result.success(createOutputData(commentId, false));
                    } else {
                        result[0] = Result.retry();
                    }
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result[0];
        } else {
            return Result.failure();
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }

    private Data createOutputData(String commentId, boolean syncPending){
        return new Data.Builder()
                .putString(Constants.KEY_COMMENT_ID, commentId)
                .putBoolean(Constants.KEY_COMMENT_SYNC_PENDING, syncPending)
                .build();
    }


    public static class Factory implements ChildWorkerFactory {

        private LocalCommentRepository localCommentRepository;
        private FirebaseFirestore firestore;

        @Inject
        public Factory(LocalCommentRepository localCommentRepository, FirebaseFirestore firestore){
            this.localCommentRepository = localCommentRepository;
            this.firestore = firestore;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new CommentSyncWorker(localCommentRepository, firestore, appContext, params);
        }
    }
}
