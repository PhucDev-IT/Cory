package com.developer.cory.data_local


import android.content.Context

import com.developer.cory.Model.Account
import com.google.gson.Gson

class DataLocalManager {


    private lateinit var mySharedPreferences: MySharedPreferences

    companion object{
        private const val PREF_OBJECT_ACCOUNT:String = "PREF_OBJECT_ACCOUNT"
        private const val PREF_TOKEN_MESSENGING:String = "PREF_TOKEN_MESSENGING"
        private var instance: DataLocalManager? = null

        fun init(context: Context) {
            instance = DataLocalManager()
            instance!!.mySharedPreferences = MySharedPreferences(context)
        }

        private fun getInstance(): DataLocalManager {
            if (instance == null) {
                instance = DataLocalManager()
            }
            return instance as DataLocalManager
        }


        fun setAccount(account: Account) {
            val gson: Gson = Gson()
            val jsonAccount = gson.toJson(account)
            getInstance().mySharedPreferences.putStringValue(PREF_OBJECT_ACCOUNT,jsonAccount)
        }

        fun getAccount(): Account? {
            val strJsonAccount: String? =
                getInstance().mySharedPreferences.getStringValues(PREF_OBJECT_ACCOUNT)
            val gson: Gson = Gson()
            return gson.fromJson(strJsonAccount, Account::class.java)?:null
        }


        fun saveTokenMessing(token:String){
            getInstance().mySharedPreferences.putStringValue(PREF_TOKEN_MESSENGING,token)

        }

        fun getToken(): String? {
            return getInstance().mySharedPreferences.getStringValues(PREF_TOKEN_MESSENGING)
        }



    }
}