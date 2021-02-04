package com.example.offlinefirst.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Comment implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "chat_id")
    private long chatId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sync_pending")
    private boolean syncPending;

    @Ignore
    public Comment(long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.syncPending = true;
    }

    public Comment(long id, long chatId, String text, long timestamp, boolean syncPending) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
    }

    protected Comment(Parcel in) {
        id = in.readLong();
        chatId = in.readLong();
        text = in.readString();
        timestamp = in.readLong();
        syncPending = in.readByte() != 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(chatId);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (syncPending ? 1 : 0));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSyncPending() {
        return syncPending;
    }

    public void setSyncPending(boolean syncPending) {
        this.syncPending = syncPending;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Comment comment = (Comment) obj;
        return comment.id == this.id
                && comment.syncPending == this.syncPending;
    }
}
