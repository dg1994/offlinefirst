package com.example.offlinefirst.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity
public class Comment implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "chat_id")
    private long chatId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sync_pending")
    private boolean syncPending;

    //ideally should be user id
    @ColumnInfo(name= "from")
    private String from;

    @Ignore
    public Comment(@NotNull String id, long chatId, String text, String from) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.from = from;
        this.timestamp = System.currentTimeMillis();
        this.syncPending = true;
    }

    public Comment(@NotNull String id, long chatId, String text, long timestamp, boolean syncPending, String from) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
        this.from = from;
    }

    protected Comment(Parcel in) {
        id = in.readString();
        chatId = in.readLong();
        text = in.readString();
        timestamp = in.readLong();
        syncPending = in.readByte() != 0;
        from = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(chatId);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (syncPending ? 1 : 0));
        dest.writeString(from);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Comment comment = (Comment) obj;
        return comment.id.equals(this.id)
                && comment.syncPending == this.syncPending;
    }
}
