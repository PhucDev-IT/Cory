package com.developer.cory.data_local


import android.content.Context
import com.developer.cory.Model.Account
import com.google.gson.Gson

class DataLocalManager {

    private lateinit var mySharedPreferences: MySharedPreferences


        private var instance:DataLocalManager?=null

        fun getInstance():DataLocalManager{
            if(instance==null){
                instance = DataLocalManager()
            }
            return instance as DataLocalManager
        }



    fun init(context:Context){
        instance = DataLocalManager()
        mySharedPreferences = MySharedPreferences(context)
    }


    fun setAccount(account: Account)
    {
        val gson:Gson = Gson()
        val JsonAccount = gson.toJson(account)

    }
}