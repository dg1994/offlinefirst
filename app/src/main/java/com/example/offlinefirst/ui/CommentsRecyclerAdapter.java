package com.example.offlinefirst.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinefirst.MainActivity;
import com.example.offlinefirst.R;
import com.example.offlinefirst.interfaces.ActionListener;
import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.utils.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentsRecyclerAdapter extends PagedListAdapter<Comment, CommentsRecyclerAdapter.CommentViewHolder> {

    private static final String TAG = "CommentListAdapter";

    private ActionListener actionListener;

    public CommentsRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    @NotNull
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called : ");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull CommentViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder called : " + position);
        final Comment comment = getItem(position);
        if (comment != null) {
            if (comment.isSyncPending()) {
                holder.commentDelete.setVisibility(View.VISIBLE);
                holder.commentSent.setVisibility(View.GONE);
            } else {
                holder.commentDelete.setVisibility(View.GONE);
                holder.commentSent.setVisibility(View.VISIBLE);
            }
            holder.commentText.setText(comment.getText());
            holder.commentDelete.setOnClickListener(v -> {
                actionListener.onCommentDelete(comment);
            });
            holder.timeStamp.setText(Helper.Companion.getDate(comment.getTimestamp()));
            holder.sentBy.setText(comment.getFrom());
        }
    }


    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView commentText;
        private AppCompatImageView commentDelete;
        private AppCompatImageView commentSent;
        private AppCompatTextView sentBy;
        private AppCompatTextView timeStamp;

        public CommentViewHolder(final View commentView) {
            super(commentView);
            commentText = commentView.findViewById(R.id.comment);
            commentDelete = commentView.findViewById(R.id.comment_delete);
            commentSent = commentView.findViewById(R.id.comment_sent);
            sentBy = commentView.findViewById(R.id.sent_by);
            timeStamp = commentView.findViewById(R.id.timestamp);
        }


    }

    public static DiffUtil.ItemCallback<Comment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Comment>() {

        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.equals(newItem);
        }
    };
}
