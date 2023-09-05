package com.developer.cory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developer.cory.Model.Account


@Database(entities = [Account::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {

    companion object {
        private val DATABASE_NAME: String = "Cory.db"
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }

    abstract fun accountDao(): AccountDAO
}