package com.example.offlinefirst;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.offlinefirst.interfaces.ActionListener;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.model.User;
import com.example.offlinefirst.network.Resource;
import com.example.offlinefirst.ui.BaseActivity;
import com.example.offlinefirst.ui.MessagesRecyclerAdapter;
import com.example.offlinefirst.ui.auth.AuthActivity;
import com.example.offlinefirst.viewmodel.ViewModelProviderFactory;
import com.example.offlinefirst.viewmodel.main.MessageViewModel;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements View.OnClickListener, ActionListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    MessagesRecyclerAdapter messagesRecyclerAdapter;

    private MessageViewModel messageViewModel;
    private AppCompatEditText messageText;
    private AppCompatButton messageBtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        messageViewModel = new ViewModelProvider(this, providerFactory).get(MessageViewModel.class);
        messageViewModel.observeAuthState().observe(this, response -> {
            Resource<User> userResource = response.getContentIfNotHandled();
            if (userResource != null) {
                if (userResource.data == null) {
                    navAuthActivity();
                }
            }
        });
        messageViewModel.getMessages().observe(this, messages -> messagesRecyclerAdapter.submitList(messages));
        messageViewModel.listenChatMessages().observe(this, messages -> {});
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerView.setAdapter(messagesRecyclerAdapter);
        messagesRecyclerAdapter.setActionListener(this);
        messagesRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart == 0) {
                    recyclerViewLayoutManager.scrollToPosition(0);
                }
            }
        });
    }

    private void initViews() {
        messageText = findViewById(R.id.message_text);
        messageBtn = findViewById(R.id.message_btn);
        recyclerView = findViewById(R.id.messages_recycler);
        messageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_btn : {
                hideKeyboard();
                if (!TextUtils.isEmpty(messageText.getText())) {
                    messageViewModel.addMessage(messageText.getText().toString());
                    messageText.getText().clear();
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
    public void onMessageDelete(Message message) {
        messageViewModel.deleteMessage(message);
    }

    private void navAuthActivity(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
