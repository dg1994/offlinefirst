package com.example.offlinefirst;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.offlinefirst.interfaces.ActionListener;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.model.User;
import com.example.offlinefirst.network.Resource;
import com.example.offlinefirst.ui.BaseActivity;
import com.example.offlinefirst.ui.CommentsRecyclerAdapter;
import com.example.offlinefirst.ui.auth.AuthActivity;
import com.example.offlinefirst.viewmodel.ViewModelProviderFactory;
import com.example.offlinefirst.viewmodel.main.CommentViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener, ActionListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    CommentsRecyclerAdapter commentsRecyclerAdapter;

    private CommentViewModel commentViewModel;
    private AppCompatEditText commentText;
    private AppCompatButton commentBtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        commentViewModel = ViewModelProviders.of(this, providerFactory).get(CommentViewModel.class);
        commentViewModel.observeAuthState().observe(this, response -> {
            Resource<User> userResource = response.getContentIfNotHandled();
            if (userResource != null) {
                if (userResource.data == null) {
                    navAuthActivity();
                }
            }
        });
        commentViewModel.getComments().observe(this, comments -> commentsRecyclerAdapter.submitList(comments));
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerView.setAdapter(commentsRecyclerAdapter);
        commentsRecyclerAdapter.setActionListener(this);
        commentsRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart == 0) {
                    recyclerViewLayoutManager.scrollToPosition(0);
                }
            }
        });
    }

    private void initViews() {
        commentText = findViewById(R.id.comment_text);
        commentBtn = findViewById(R.id.comment_btn);
        recyclerView = findViewById(R.id.comments_recycler);
        commentBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_btn : {
                hideKeyboard();
                if (!TextUtils.isEmpty(commentText.getText())) {
                    commentViewModel.addComment(commentText.getText().toString());
                    commentText.getText().clear();
                }
                break;
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onCommentDelete(Comment comment) {
        commentViewModel.deleteComment(comment);
    }

    private void navAuthActivity(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
