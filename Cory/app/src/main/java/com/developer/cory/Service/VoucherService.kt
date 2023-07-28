package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.util.Log
import com.developer.cory.Model.Voucher
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VoucherService {
    private val db = Firebase.firestore

    fun selectVoucher(callback:(List<Voucher>)->Unit){
        db.collection("Vouchers").get()
            .addOnSuccessListener { result->
                var list = mutableListOf<Voucher>()
                for (document in result){
                    var voucher = document.toObject(Voucher::class.java)
                    voucher.idVoucher = document.id
                    list.add(voucher)
                    Log.d(TAG,"$voucher")
                }
                callback(list)
            }
            .addOnFailureListener { er->
                Log.e(TAG,"Có lỗi: ${er.message}")
                callback(emptyList())
            }
    }
}