package com.example.offlinefirst.domain.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.offlinefirst.model.Comment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BaseCommentRepository {
    private LocalCommentRepository localCommentRepository;
    private RemoteCommentRepository remoteCommentRepository;

    public BaseCommentRepository(LocalCommentRepository localCommentRepository,
                                 RemoteCommentRepository remoteCommentRepository) {
        this.localCommentRepository = localCommentRepository;
        this.remoteCommentRepository = remoteCommentRepository;
    }

    public Completable add(long chatId, String commentText) {
        return localCommentRepository.add(chatId, commentText)
                .flatMapCompletable(comment -> remoteCommentRepository.sync(comment));
    }

    public List<Comment> getComments(long chatId, int requestLoadSize) {
        return localCommentRepository.getComments(chatId, requestLoadSize);
    }

    public List<Comment> getCommentsAfter(long chatId, long timestamp, int requestLoadSize) {
        return localCommentRepository.getCommentsAfter(chatId, timestamp, requestLoadSize);
    }

    public DataSource.Factory<Integer, Comment> allComments(long chatId) {
        return localCommentRepository.allComments(chatId);
    }

    public Completable delete(Comment comment) {
        return remoteCommentRepository.stopSync(comment)
                .andThen(Completable.defer(() -> localCommentRepository.delete(comment)));
    }

    public LiveData<List<Comment>> listenChatMessages() {
        MediatorLiveData<List<Comment>> messagesLiveData = new MediatorLiveData<>();
        MutableLiveData<List<Comment>> firestoreSource = remoteCommentRepository.listenFirestoreDb();
        messagesLiveData.addSource(firestoreSource, comments -> {
            localCommentRepository.addAll(comments);
        });
        return messagesLiveData;
    }
}
