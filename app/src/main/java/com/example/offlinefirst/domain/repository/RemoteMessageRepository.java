package com.example.offlinefirst.domain.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.Constants;
import com.example.offlinefirst.workmanager.MessageSaveWorker;
import com.example.offlinefirst.workmanager.MessageSyncWorker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RemoteMessageRepository {

    private Context context;
    private FirebaseFirestore firestoreDb;
    private static final String SYNC_TAG = "message-sync";
    private static final String SAVE_TAG = "message-save";

    public RemoteMessageRepository(Context applicationContext, FirebaseFirestore firestoreDb) {
        context = applicationContext;
        this.firestoreDb = firestoreDb;
    }

    public Completable sync(Message message) {

        return Completable.fromAction(() -> {
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            Data data = new Data.Builder()
                    .putString(Constants.KEY_COMMENT_ID, message.getId())
                    .build();
            OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(MessageSyncWorker.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .addTag(SYNC_TAG + message.getId())
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                    .build();
            OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(MessageSaveWorker.class)
                    .setInputData(data)
                    .addTag(SAVE_TAG + message.getId())
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 2, TimeUnit.SECONDS)
                    .build();

            WorkManager.getInstance(context)
                    .beginWith(syncRequest)
                    .then(saveRequest)
                    .enqueue();
        });

//        LiveData<List<WorkInfo>> workInfosByTagLiveData = WorkManager.getInstance(context)
//                .getWorkInfosByTagLiveData(SAVE_TAG + message.getId());
//        return new LiveDataObservable<>(workInfosByTagLiveData, null);
    }

    public Completable stopSync(Message message) {

        return Observable.just(message)
                .flatMapCompletable(it -> {
                    if (!isSyncPending(it.getId())) {
                        throw new Exception("sync is not pending, cannot delete message");
                    }
                    return Completable.fromAction(() -> {
                        WorkManager.getInstance(context)
                                .cancelAllWorkByTag(SYNC_TAG + message.getId());
                    });
                });
    }

    private boolean isSyncPending(String messageId) {
        try {
            boolean running = false;
            WorkManager instance = WorkManager.getInstance(context);
            ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(SYNC_TAG + messageId);
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING ||
                        state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MutableLiveData<List<Message>> listenFirestoreDb() {
        MutableLiveData<List<Message>> chatLiveData = new MutableLiveData<>();
        firestoreDb.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("RemoteMessageRepository", "got error while fetching" + error.getMessage());
                } else {
                    if (value != null) {
                        List<Message> messageList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Message message = document.toObject(Message.class);
                            messageList.add(message);
                        }
                        chatLiveData.setValue(messageList);
                        Log.d("RemoteMessageRepository", "value  set");
                    }
                }
            }
        });
        return chatLiveData;
    }
}
