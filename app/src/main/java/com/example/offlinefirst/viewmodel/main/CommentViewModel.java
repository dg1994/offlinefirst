package com.example.offlinefirst.viewmodel.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.offlinefirst.domain.datasource.CommentDataSourceFactory;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.domain.repository.BaseCommentRepository;
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

public class CommentViewModel extends ViewModel {

    private static final String TAG = "CommentViewModel";

    private BaseCommentRepository commentRepository;
    private CommentDataSourceFactory commentDataSourceFactory;
    private LiveData<PagedList<Comment>> commentsLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;

    @Inject
    public CommentViewModel(
            BaseCommentRepository commentRepository,
            CommentDataSourceFactory commentDataSourceFactory,
            SessionManager sessionManager
    ) {
        this.commentRepository = commentRepository;
        this.commentDataSourceFactory = commentDataSourceFactory;
        this.sessionManager = sessionManager;
        initPaging();
    }

    private void initPaging() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(10)
                        .build();

        //commentsLiveData = new LivePagedListBuilder<>(commentDataSourceFactory, pagedListConfig).build();
        commentsLiveData = new LivePagedListBuilder<>(commentRepository.allComments(CHAT_ID), pagedListConfig).build();
    }

    /**
     * Adds new comment
     */
    public void addComment(String commentText) {
        disposable.add(commentRepository.add(CHAT_ID, commentText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "comment added ");
                    },
                        t -> Log.e(TAG, "add comment error : " + t)
                )
        );
    }

    /**
     * Exposes the latest comments so that UI can observe it
     */
    public LiveData<PagedList<Comment>> getComments() {
        return commentsLiveData;
    }

    /**
     * Delete the comment
     */
    public void deleteComment(Comment comment) {
        disposable.add(commentRepository.delete(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "comment deleted "),
                        t -> Log.e(TAG, "delete comment error : " + t)));
    }

    public void logout() {
        sessionManager.logout();
    }

    public LiveData<Event<Resource<User>>> observeAuthState() {
        return sessionManager.getCachedUser();
    }

    public LiveData<List<Comment>> listenChatMessages() {
        return commentRepository.listenChatMessages();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        //commentDataSourceFactory.getCommentDataSource().clear();
    }

}
