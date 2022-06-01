package com.example.offlinefirst.domain.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Constants;
import com.example.offlinefirst.utils.LiveDataObservable;
import com.example.offlinefirst.workmanager.CommentSaveWorker;
import com.example.offlinefirst.workmanager.CommentSyncWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RemoteCommentRepository {

    private Context context;
    private FirebaseFirestore firestoreDb;
    private static final String SYNC_TAG = "comment-sync";
    private static final String SAVE_TAG = "comment-save";

    public RemoteCommentRepository(Context applicationContext, FirebaseFirestore firestoreDb) {
        context = applicationContext;
        this.firestoreDb = firestoreDb;
    }

    public Completable sync(Comment comment) {

        return Completable.fromAction(() -> {
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            Data data = new Data.Builder()
                    .putString(Constants.KEY_COMMENT_ID, comment.getId())
                    .build();
            OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(CommentSyncWorker.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .addTag(SYNC_TAG + comment.getId())
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                    .build();
            OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(CommentSaveWorker.class)
                    .setInputData(data)
                    .addTag(SAVE_TAG + comment.getId())
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 2, TimeUnit.SECONDS)
                    .build();

            WorkManager.getInstance(context)
                    .beginWith(syncRequest)
                    .then(saveRequest)
                    .enqueue();
        });

//        LiveData<List<WorkInfo>> workInfosByTagLiveData = WorkManager.getInstance(context)
//                .getWorkInfosByTagLiveData(SAVE_TAG + comment.getId());
//        return new LiveDataObservable<>(workInfosByTagLiveData, null);
    }

    public Completable stopSync(Comment comment) {

        return Observable.just(comment)
                .flatMapCompletable(it -> {
                    if (!isSyncPending(it.getId())) {
                        throw new Exception("sync is not pending, cannot delete comment");
                    }
                    return Completable.fromAction(() -> {
                        WorkManager.getInstance(context)
                                .cancelAllWorkByTag(SYNC_TAG + comment.getId());
                    });
                });
    }

    private boolean isSyncPending(String commentId) {
        try {
            boolean running = false;
            WorkManager instance = WorkManager.getInstance(context);
            ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(SYNC_TAG + commentId);
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

    public MutableLiveData<List<Comment>> listenFirestoreDb() {
        MutableLiveData<List<Comment>> chatLiveData = new MutableLiveData<>();
        firestoreDb.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("RemoteCommentRepository", "got error while fetching" + error.getMessage());
                } else {
                    if (value != null) {
                        List<Comment> commentList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            commentList.add(comment);
                        }
                        chatLiveData.setValue(commentList);
                        Log.d("RemoteCommentRepository", "value  set");
                    }
                }
            }
        });
        return chatLiveData;
    }
}
