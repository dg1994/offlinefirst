package com.example.offlinefirst.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.offlinefirst.domain.datasource.CommentDataSourceFactory;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.model.Post;
import com.example.offlinefirst.network.main.CommentApi;
import com.example.offlinefirst.domain.repository.LocalCommentRepository;
import com.example.offlinefirst.utils.CommentUtils;
import com.example.offlinefirst.utils.Constants;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.offlinefirst.utils.Constants.CHAT_ID;

public class CommentSyncWorker extends Worker {

    public static final String TAG = "CommentSyncWorker";
    private LocalCommentRepository localCommentRepository;
    private CommentApi commentApi;
    private Call<List<Post>> syncCommentCall;

    public CommentSyncWorker(LocalCommentRepository localCommentRepository, CommentApi commentApi,
                             Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        this.localCommentRepository = localCommentRepository;
        this.commentApi = commentApi;
    }

    @NonNull
    @Override
    public Result doWork() {

        long commentId = getInputData().getLong(Constants.KEY_COMMENT_ID, 1);
        Comment comment = localCommentRepository.getComment(commentId);

        Response<List<Post>> response = null;
        try {
            if (!isStopped()) {
                //dummy call to imitate sending content to server
                syncCommentCall = commentApi.getPostsFromUser(1);
                response = syncCommentCall.execute();

                if (response.isSuccessful()) {
                    return Result.success(createOutputData(commentId, false));
                } else {
                    if (response.code() >= 500 && response.code() <= 599) {
                        // try again if there is a server error
                        return Result.retry();
                    }
                    return Result.failure();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.failure();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        syncCommentCall.cancel();
    }

    private Data createOutputData(long commentId, boolean syncPending){
        return new Data.Builder()
                .putLong(Constants.KEY_COMMENT_ID, commentId)
                .putBoolean(Constants.KEY_COMMENT_SYNC_PENDING, syncPending)
                .build();
    }


    public static class Factory implements ChildWorkerFactory {

        private LocalCommentRepository localCommentRepository;
        private CommentApi commentApi;

        @Inject
        public Factory(LocalCommentRepository localCommentRepository, CommentApi commentApi){
            this.localCommentRepository = localCommentRepository;
            this.commentApi = commentApi;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new CommentSyncWorker(localCommentRepository, commentApi, appContext, params);
        }
    }
}
