package com.example.offlinefirst.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "auth_token",  foreignKeys = {@ForeignKey(entity = AccountProperties.class,
        parentColumns = "pk",
        childColumns = "account_pk",
        onDelete = ForeignKey.CASCADE)
})
public class AuthToken implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "account_pk")
    private int accountPk =-1;

    @SerializedName("token")
    @Expose
    @ColumnInfo(name = "token")
    private String token;

    protected AuthToken(Parcel in) {
        accountPk = in.readInt();
        token = in.readString();
    }

    public AuthToken(int accountPk, String token) {
        this.accountPk = accountPk;
        this.token = token;
    }

    public static final Creator<AuthToken> CREATOR = new Creator<AuthToken>() {
        @Override
        public AuthToken createFromParcel(Parcel in) {
            return new AuthToken(in);
        }

        @Override
        public AuthToken[] newArray(int size) {
            return new AuthToken[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(accountPk);
        parcel.writeString(token);
    }

    public int getAccountPk() {
        return accountPk;
    }

    public void setAccountPk(int accountPk) {
        this.accountPk = accountPk;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
