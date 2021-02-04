package com.example.offlinefirst.domain.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.offlinefirst.model.Comment;

import javax.inject.Inject;

public class CommentDataSourceFactory extends DataSource.Factory<Long, Comment>{
    private CommentDataSource commentDataSource;
    private MutableLiveData<CommentDataSource> commentDataSourceLiveData;

    @Inject
    public CommentDataSourceFactory(CommentDataSource commentDataSource) {
        this.commentDataSource = commentDataSource;
        commentDataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Comment> create() {
        commentDataSourceLiveData.postValue(commentDataSource);
        return commentDataSource;
    }

    public CommentDataSource getCommentDataSource() {
        return commentDataSource;
    }

    public void refresh() {
        if (commentDataSourceLiveData.getValue() != null)
            commentDataSourceLiveData.getValue().invalidate();
    }
}
