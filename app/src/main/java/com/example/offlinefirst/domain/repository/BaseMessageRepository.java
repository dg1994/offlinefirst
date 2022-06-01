package com.example.offlinefirst.domain.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.offlinefirst.model.Message;

import java.util.List;

import io.reactivex.Completable;

public class BaseMessageRepository {
    private LocalMessageRepository localMessageRepository;
    private RemoteMessageRepository remoteMessageRepository;

    public BaseMessageRepository(LocalMessageRepository localMessageRepository,
                                 RemoteMessageRepository remoteMessageRepository) {
        this.localMessageRepository = localMessageRepository;
        this.remoteMessageRepository = remoteMessageRepository;
    }

    public Completable add(long chatId, String messageText) {
        return localMessageRepository.add(chatId, messageText)
                .flatMapCompletable(message -> remoteMessageRepository.sync(message));
    }

    public List<Message> getMessages(long chatId, int requestLoadSize) {
        return localMessageRepository.getMessages(chatId, requestLoadSize);
    }

    public List<Message> getMessagesAfter(long chatId, long timestamp, int requestLoadSize) {
        return localMessageRepository.getMessagesAfter(chatId, timestamp, requestLoadSize);
    }

    public DataSource.Factory<Integer, Message> allMessages(long chatId) {
        return localMessageRepository.allMessages(chatId);
    }

    public Completable delete(Message message) {
        return remoteMessageRepository.stopSync(message)
                .andThen(Completable.defer(() -> localMessageRepository.delete(message)));
    }

    public LiveData<List<Message>> listenChatMessages() {
        MediatorLiveData<List<Message>> messagesLiveData = new MediatorLiveData<>();
        MutableLiveData<List<Message>> firestoreSource = remoteMessageRepository.listenFirestoreDb();
        messagesLiveData.addSource(firestoreSource, messages -> {
            localMessageRepository.addAll(messages);
        });
        return messagesLiveData;
    }
}
