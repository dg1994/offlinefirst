package com.example.offlinefirst.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinefirst.R;
import com.example.offlinefirst.interfaces.ActionListener;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.Helper;

import org.jetbrains.annotations.NotNull;

public class MessagesRecyclerAdapter extends PagedListAdapter<Message, MessagesRecyclerAdapter.MessageViewHolder> {

    private static final String TAG = "MessageRecyclerAdapter";

    private ActionListener actionListener;

    public MessagesRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    @NotNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called : ");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull MessageViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder called : " + position);
        final Message message = getItem(position);
        if (message != null) {
            if (message.isSyncPending()) {
                holder.messageDelete.setVisibility(View.VISIBLE);
                holder.messageSent.setVisibility(View.GONE);
            } else {
                holder.messageDelete.setVisibility(View.GONE);
                holder.messageSent.setVisibility(View.VISIBLE);
            }
            holder.messageText.setText(message.getText());
            holder.messageDelete.setOnClickListener(v -> {
                actionListener.onMessageDelete(message);
            });
            holder.timeStamp.setText(Helper.Companion.getDate(message.getTimestamp()));
            holder.sentBy.setText(message.getFrom());
        }
    }


    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView messageText;
        private AppCompatImageView messageDelete;
        private AppCompatImageView messageSent;
        private AppCompatTextView sentBy;
        private AppCompatTextView timeStamp;

        public MessageViewHolder(final View commentView) {
            super(commentView);
            messageText = commentView.findViewById(R.id.message);
            messageDelete = commentView.findViewById(R.id.message_delete);
            messageSent = commentView.findViewById(R.id.message_sent);
            sentBy = commentView.findViewById(R.id.sent_by);
            timeStamp = commentView.findViewById(R.id.timestamp);
        }


    }

    public static DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {

        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };
}
