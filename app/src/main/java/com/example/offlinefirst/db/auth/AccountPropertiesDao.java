package com.example.offlinefirst.db.auth;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.offlinefirst.model.AccountProperties;

@Dao
public interface AccountPropertiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAndReplace(AccountProperties accountProperties);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertOrIgnore(AccountProperties accountProperties);

    @Query("SELECT * FROM account_properties where pk = :pk")
    LiveData<AccountProperties> searchByPk(int pk);

    @Query("SELECT * FROM account_properties where email = :email")
    AccountProperties searchByEmail(String email);

}
