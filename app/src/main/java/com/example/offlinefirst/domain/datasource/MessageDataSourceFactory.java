package com.example.offlinefirst.domain.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.offlinefirst.model.Message;

import javax.inject.Inject;

public class MessageDataSourceFactory extends DataSource.Factory<Long, Message>{
    private MessageDataSource messageDataSource;
    private MutableLiveData<MessageDataSource> commentDataSourceLiveData;

    @Inject
    public MessageDataSourceFactory(MessageDataSource messageDataSource) {
        this.messageDataSource = messageDataSource;
        commentDataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Message> create() {
        commentDataSourceLiveData.postValue(messageDataSource);
        return messageDataSource;
    }

    public MessageDataSource getCommentDataSource() {
        return messageDataSource;
    }

    public void refresh() {
        if (commentDataSourceLiveData.getValue() != null)
            commentDataSourceLiveData.getValue().invalidate();
    }
}
