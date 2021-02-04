package com.example.offlinefirst.domain.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.example.offlinefirst.domain.repository.BaseCommentRepository;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentDataSource extends ItemKeyedDataSource<Long, Comment> {

    private BaseCommentRepository baseCommentRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CommentDataSource(BaseCommentRepository baseCommentRepository) {
        this.baseCommentRepository = baseCommentRepository;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Comment> callback) {
        List<Comment> comments = baseCommentRepository.getComments(Constants.CHAT_ID, params.requestedLoadSize);
        callback.onResult(comments);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Comment> callback) {
        List<Comment> comments = baseCommentRepository.getCommentsAfter(Constants.CHAT_ID, params.key, params.requestedLoadSize);
        callback.onResult(comments);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Comment> callback) {

    }

    @NonNull
    @Override
    public Long getKey(@NonNull Comment item) {
        return item.getTimestamp();
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
