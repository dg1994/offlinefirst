package com.example.offlinefirst.domain.repository;

import android.util.Log;

import androidx.paging.DataSource;

import com.example.offlinefirst.db.CommentDao;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.CommentUtils;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalCommentRepository {

    private static final String TAG = "LocalCommentRepository";

    private CommentDao commentDao;

    public LocalCommentRepository(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    
    public Single<Comment> add(long chatId, String commentText) {
        Comment comment = new Comment(chatId, commentText);

        return Single.fromCallable(() -> {
            long rowId = commentDao.add(comment);
            Log.d(TAG, "comment added " + rowId);
            return CommentUtils.clone(comment, rowId);
        });
    }

    public Completable updateCompletable(Comment comment) {
        return Completable.fromAction(() -> commentDao.update(comment));
    }

    public void update(Comment comment) {
        commentDao.update(comment);
    }

    public Completable delete(Comment comment) {
        return Completable.fromAction(() -> commentDao.delete(comment));
    }

    public List<Comment> getComments(long chatId, int requestLoadSize) {
        return commentDao.getComments(chatId, requestLoadSize);
    }

    public List<Comment> getCommentsAfter(long chatId, long timestamp, int requestLoadSize) {
        return commentDao.getCommentsAfter(chatId, timestamp, requestLoadSize);
    }

    public DataSource.Factory<Integer, Comment> allComments(long chatId) {
        return commentDao.allComments(chatId);
    }

    public Comment getComment(long commentId) {
        return commentDao.getComment(commentId);
    }
}
