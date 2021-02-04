package com.example.offlinefirst.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.offlinefirst.utils.Constants.CHAT_ID;

public class CommentSyncWorker extends Worker {

    public static final String TAG = "CommentSyncWorker";
    private LocalCommentRepository localCommentRepository;
    private CommentApi commentApi;
    private CommentDataSourceFactory commentDataSourceFactory;

    public CommentSyncWorker(LocalCommentRepository localCommentRepository, CommentApi commentApi,
                             CommentDataSourceFactory commentDataSourceFactory, Context context,
                             WorkerParameters workerParams) {
        super(context, workerParams);
        this.localCommentRepository = localCommentRepository;
        this.commentApi = commentApi;
        this.commentDataSourceFactory = commentDataSourceFactory;
    }

    @NonNull
    @Override
    public Result doWork() {

        long commentId = getInputData().getLong(Constants.KEY_COMMENT_ID, 1);
        Comment comment = localCommentRepository.getComment(commentId);

        Response<List<Post>> response = null;
        try {
            response = commentApi.getPostsFromUser(1).execute();
            if (response.isSuccessful()) {
                //Comment updatedComment = CommentUtils.clone(comment, false);
                comment.setSyncPending(false);
                localCommentRepository.update(comment);
//                Disposable updateDisposable = localCommentRepository.updateCompletable(comment)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(() -> {
//                                    Log.d(TAG, "comment updated ");
//                                    commentDataSourceFactory.refresh();
//                                },
//                                t -> Log.e(TAG, "update comment error : " + t)
//                        );
                return Result.success();
            } else {
                if (response.code() >= 500 && response.code() <= 599) {
                    // try again if there is a server error
                    return Result.retry();
                }
                return Result.failure();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }


    public static class Factory implements ChildWorkerFactory {

        private LocalCommentRepository localCommentRepository;
        private CommentApi commentApi;
        private CommentDataSourceFactory commentDataSourceFactory;

        @Inject
        public Factory(LocalCommentRepository localCommentRepository, CommentApi commentApi,
                       CommentDataSourceFactory commentDataSourceFactory){
            this.localCommentRepository = localCommentRepository;
            this.commentApi = commentApi;
            this.commentDataSourceFactory = commentDataSourceFactory;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new CommentSyncWorker(localCommentRepository, commentApi, commentDataSourceFactory,
                    appContext, params);
        }
    }
}
