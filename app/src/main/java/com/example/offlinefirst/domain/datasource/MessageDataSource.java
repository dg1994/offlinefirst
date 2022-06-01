package com.example.offlinefirst.domain.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.example.offlinefirst.domain.repository.BaseMessageRepository;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MessageDataSource extends ItemKeyedDataSource<Long, Message> {

    private BaseMessageRepository baseMessageRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public MessageDataSource(BaseMessageRepository baseMessageRepository) {
        this.baseMessageRepository = baseMessageRepository;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Message> callback) {
        List<Message> messages = baseMessageRepository.getMessages(Constants.CHAT_ID, params.requestedLoadSize);
        callback.onResult(messages);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Message> callback) {
        List<Message> messages = baseMessageRepository.getMessagesAfter(Constants.CHAT_ID, params.key, params.requestedLoadSize);
        callback.onResult(messages);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Message> callback) {

    }

    @NonNull
    @Override
    public Long getKey(@NonNull Message item) {
        return item.getTimestamp();
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
