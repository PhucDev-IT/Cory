package com.developer.cory.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.developer.cory.Model.Account

@Dao
interface AccountDAO {
    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(account: Account)

    @Query("SELECT * FROM Account")
    fun selectAccount():Account?
}