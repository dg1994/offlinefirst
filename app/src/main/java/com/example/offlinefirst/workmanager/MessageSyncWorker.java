package com.example.offlinefirst.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.offlinefirst.domain.repository.LocalMessageRepository;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

public class MessageSyncWorker extends Worker {

    public static final String TAG = "MessageSyncWorker";
    private LocalMessageRepository localMessageRepository;
    private FirebaseFirestore firestore;

    public MessageSyncWorker(LocalMessageRepository localMessageRepository, FirebaseFirestore firestore,
                             Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        this.localMessageRepository = localMessageRepository;
        this.firestore = firestore;
    }

    @NonNull
    @Override
    public Result doWork() {

        String messageId = getInputData().getString(Constants.KEY_COMMENT_ID);
        Message message = localMessageRepository.getMessage(messageId);

        final Result[] result = {Result.retry()};
        CountDownLatch countDownLatch = new CountDownLatch(1);
        if (!isStopped()) {
            DocumentReference newMessageRef = firestore.collection("chats")
                    .document();

            newMessageRef.set(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        result[0] = Result.success(createOutputData(messageId, false));
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

    private Data createOutputData(String messageId, boolean syncPending){
        return new Data.Builder()
                .putString(Constants.KEY_COMMENT_ID, messageId)
                .putBoolean(Constants.KEY_COMMENT_SYNC_PENDING, syncPending)
                .build();
    }


    public static class Factory implements ChildWorkerFactory {

        private LocalMessageRepository localMessageRepository;
        private FirebaseFirestore firestore;

        @Inject
        public Factory(LocalMessageRepository localMessageRepository, FirebaseFirestore firestore){
            this.localMessageRepository = localMessageRepository;
            this.firestore = firestore;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new MessageSyncWorker(localMessageRepository, firestore, appContext, params);
        }
    }
}
