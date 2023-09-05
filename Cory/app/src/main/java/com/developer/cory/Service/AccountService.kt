package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.developer.cory.MainActivity
import com.developer.cory.Model.Account
import com.developer.cory.Model.Temp
import com.developer.cory.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class AccountService(private val db: FirebaseFirestore) {

    //Đăng ký người dùng
    fun addAccount(account: Account,user: User, callback: (b: Boolean) -> Unit) {
        db.collection("account").document(account.email!!).set(account)
            .addOnSuccessListener {
                addUser(user){b->
                    if(b) callback(true)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Lỗi: ${it.message}")
                callback(false)
            }
    }

    private fun addUser(user: User, callback: (b: Boolean) -> Unit) {
        db.collection("Users").add(user)
            .addOnCompleteListener {
                callback(true)
            }
            .addOnFailureListener {
                Log.e("Lỗi: ", "Không thể xóa: ${it.message} ")

                callback(false)
            }
    }

    //Login
     fun login(taiKhoan:String,password:String,callback:(b:Boolean)->Unit){
        db.collection("account").document(taiKhoan)
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val fieldValue = documentSnapshot.getString("password")
                    if(password == fieldValue){
                        Temp.account = documentSnapshot.toObject(Account::class.java)!!
                        callback(true)
                    }else{
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Lỗi: ","${exception.message}")
            }
    }
}