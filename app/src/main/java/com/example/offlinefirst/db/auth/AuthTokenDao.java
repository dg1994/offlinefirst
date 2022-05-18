package com.example.offlinefirst.db.auth;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.offlinefirst.model.AuthToken;

@Dao
public interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AuthToken authToken);

    @Query("UPDATE auth_token SET  token = null WHERE account_pk = :pk")
    int nullifyToken(int pk);

}
