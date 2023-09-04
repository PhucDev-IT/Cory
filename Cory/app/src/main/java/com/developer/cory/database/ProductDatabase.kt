package com.developer.cory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developer.cory.Model.Product

//@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {

//    companion object {
//        private val DATABASE_NAME: String = "Cory.db"
//        private var INSTANCE: ProductDatabase? = null
//
//        fun getInstance(context: Context): ProductDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(
//                    context.applicationContext,
//                    ProductDatabase::class.java,
//                    DATABASE_NAME
//                )
//                    .allowMainThreadQueries()
//                    .build()
//            }
//            return INSTANCE!!
//        }
//    }
//
//    abstract fun productDao(): ProductDAO
}