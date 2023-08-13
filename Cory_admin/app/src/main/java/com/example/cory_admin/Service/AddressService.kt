package com.example.cory_admin.Service

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cory_admin.Model.Address

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AddressService() {

    private val db = Firebase.firestore

    fun insertAddress(address: Address, idUser: String, callback: (Boolean)->Unit){
        db.collection("Users").document(idUser).collection("Address")
            .add(address)
            .addOnCompleteListener {
                callback(true)
            }
            .addOnFailureListener { error->
                Log.e(TAG,"Thêm address thất bại: ${error.message}")
                callback(false)
            }
    }

    fun selectByIDUser(idUser:String, callback: (List<Address>) -> Unit){
        var list = mutableListOf<Address>()
        db.collection("Users").document(idUser).collection("Address")
            .get()
            .addOnSuccessListener { result->
                for (document in result){
                    var address:Address = document.toObject()
                    address.id = document.id
                    address.isDefault = document.get("isDefault") as Boolean?
                    list.add(address)
                }
                callback(list)
            }
            .addOnFailureListener {
                callback(emptyList())
            }

    }
}