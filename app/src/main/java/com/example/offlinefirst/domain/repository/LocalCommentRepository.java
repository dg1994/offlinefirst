package com.example.offlinefirst.domain.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.paging.DataSource;

import com.example.offlinefirst.db.CommentDao;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.CommentUtils;
import com.example.offlinefirst.utils.Helper;
import com.example.offlinefirst.utils.PreferenceKeys;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalCommentRepository {

    private static final String TAG = "LocalCommentRepository";

    private CommentDao commentDao;
    private SharedPreferences sharedPreferences;
    public LocalCommentRepository(
            CommentDao commentDao,
            SharedPreferences sharedPreferences) {
        this.commentDao = commentDao;
        this.sharedPreferences = sharedPreferences;
    }
    
    public Single<Comment> add(long chatId, String commentText) {
        return Single.fromCallable(() -> {
            String commentId = Helper.Companion.generateId(20);
            Log.d("lcr comment  id", commentId);
            Comment comment = new Comment(
                    commentId,
                    chatId,
                    commentText,
                    sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null));
            long rowId = commentDao.add(comment);
            Log.d(TAG, "comment added " + rowId);
            return comment;
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

    public Comment getComment(String commentId) {
        return commentDao.getComment(commentId);
    }
}
