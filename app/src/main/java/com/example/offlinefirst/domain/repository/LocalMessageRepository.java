package com.example.offlinefirst.domain.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.paging.DataSource;

import com.example.offlinefirst.db.MessageDao;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.Helper;
import com.example.offlinefirst.utils.PreferenceKeys;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocalMessageRepository {

    private static final String TAG = "LocalCommentRepository";

    private MessageDao messageDao;
    private SharedPreferences sharedPreferences;
    public LocalMessageRepository(
            MessageDao messageDao,
            SharedPreferences sharedPreferences) {
        this.messageDao = messageDao;
        this.sharedPreferences = sharedPreferences;
    }
    
    public Single<Message> add(long chatId, String messageText) {
        return Single.fromCallable(() -> {
            String messageId = Helper.Companion.generateId(20);
            Message message = new Message(
                    messageId,
                    chatId,
                    messageText,
                    sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null));
            long rowId = messageDao.add(message);
            Log.d(TAG, "message added " + rowId);
            return message;
        });
    }

    public void addAll(List<Message> messages) {
        Single.fromCallable(() -> {
            List<Long> rows = messageDao.addAll(messages);
            return rows;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((messageRows) -> {
                            Log.d(TAG, "comments added ");
                        },
                        t -> Log.e(TAG, "add comments error : " + t));
    }

    public Completable updateCompletable(Message message) {
        return Completable.fromAction(() -> messageDao.update(message));
    }

    public void update(Message message) {
        messageDao.update(message);
    }

    public Completable delete(Message message) {
        return Completable.fromAction(() -> messageDao.delete(message));
    }

    public List<Message> getMessages(long chatId, int requestLoadSize) {
        return messageDao.getMessages(chatId, requestLoadSize);
    }

    public List<Message> getMessagesAfter(long chatId, long timestamp, int requestLoadSize) {
        return messageDao.getMessagesAfter(chatId, timestamp, requestLoadSize);
    }

    public DataSource.Factory<Integer, Message> allMessages(long chatId) {
        return messageDao.allMessages(chatId);
    }

    public Message getMessage(String messageId) {
        return messageDao.getMessage(messageId);
    }
}
