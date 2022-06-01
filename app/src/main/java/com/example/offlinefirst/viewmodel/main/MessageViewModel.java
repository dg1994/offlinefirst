package com.example.offlinefirst.viewmodel.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.offlinefirst.domain.datasource.MessageDataSourceFactory;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.domain.repository.BaseMessageRepository;
import com.example.offlinefirst.model.User;
import com.example.offlinefirst.network.Resource;
import com.example.offlinefirst.session.SessionManager;
import com.example.offlinefirst.utils.Event;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.offlinefirst.utils.Constants.CHAT_ID;

public class MessageViewModel extends ViewModel {

    private static final String TAG = "MessageViewModel";

    private BaseMessageRepository messageRepository;
    private MessageDataSourceFactory messageDataSourceFactory;
    private LiveData<PagedList<Message>> messagesLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;

    @Inject
    public MessageViewModel(
            BaseMessageRepository messageRepository,
            MessageDataSourceFactory messageDataSourceFactory,
            SessionManager sessionManager
    ) {
        this.messageRepository = messageRepository;
        this.messageDataSourceFactory = messageDataSourceFactory;
        this.sessionManager = sessionManager;
        initPaging();
    }

    private void initPaging() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(10)
                        .build();

        //messagesLiveData = new LivePagedListBuilder<>(messageDataSourceFactory, pagedListConfig).build();
        messagesLiveData = new LivePagedListBuilder<>(messageRepository.allMessages(CHAT_ID), pagedListConfig).build();
    }

    /**
     * Adds new message
     */
    public void addMessage(String messageText) {
        disposable.add(messageRepository.add(CHAT_ID, messageText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "message added ");
                    },
                        t -> Log.e(TAG, "add message error : " + t)
                )
        );
    }

    /**
     * Exposes the latest messages so that UI can observe it
     */
    public LiveData<PagedList<Message>> getMessages() {
        return messagesLiveData;
    }

    /**
     * Delete the comment
     */
    public void deleteMessage(Message message) {
        disposable.add(messageRepository.delete(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "message deleted "),
                        t -> Log.e(TAG, "delete message error : " + t)));
    }

    public void logout() {
        sessionManager.logout();
    }

    public LiveData<Event<Resource<User>>> observeAuthState() {
        return sessionManager.getCachedUser();
    }

    public LiveData<List<Message>> listenChatMessages() {
        return messageRepository.listenChatMessages();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        //messageDataSourceFactory.getMessageDataSource().clear();
    }

}
