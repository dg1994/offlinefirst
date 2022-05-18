package com.example.offlinefirst.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "account_properties")
public class AccountProperties implements Parcelable {
    @SerializedName("pk")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    private int pk;

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username")
    private String username;

    protected AccountProperties(Parcel in) {
        pk = in.readInt();
        email = in.readString();
        username = in.readString();
    }

    public AccountProperties(int pk, String email, String username) {
        this.pk = pk;
        this.email = email;
        this.username = username;
    }

    public static final Creator<AccountProperties> CREATOR = new Creator<AccountProperties>() {
        @Override
        public AccountProperties createFromParcel(Parcel in) {
            return new AccountProperties(in);
        }

        @Override
        public AccountProperties[] newArray(int size) {
            return new AccountProperties[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pk);
        parcel.writeString(email);
        parcel.writeString(username);
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
