package com.example.offlinefirst.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.offlinefirst.domain.repository.LocalCommentRepository;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.CommentUtils;
import com.example.offlinefirst.utils.Constants;

import javax.inject.Inject;

public class CommentSaveWorker extends Worker {

    private LocalCommentRepository localCommentRepository;

    public CommentSaveWorker(LocalCommentRepository localCommentRepository,
                             @NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.localCommentRepository = localCommentRepository;
    }

    @NonNull
    @Override
    public Result doWork() {
        long commentId = getInputData().getLong(Constants.KEY_COMMENT_ID, 1);
        boolean syncPending = getInputData().getBoolean(Constants.KEY_COMMENT_SYNC_PENDING, false);
        Comment comment = localCommentRepository.getComment(commentId);
        Comment updatedComment = CommentUtils.clone(comment, syncPending);
        localCommentRepository.update(updatedComment);
        return Result.success();
    }

    public static class Factory implements ChildWorkerFactory {

        private LocalCommentRepository localCommentRepository;

        @Inject
        public Factory(LocalCommentRepository localCommentRepository){
            this.localCommentRepository = localCommentRepository;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new CommentSaveWorker(localCommentRepository, appContext, params);
        }
    }
}
