package com.example.cory_admin.Service

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.cory_admin.Model.Voucher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.storage.FirebaseStorage

class VoucherService() {
    private var lastKey: String? = null
    private val dbRealtime = FirebaseDatabase.getInstance().getReference("Vouchers")

    fun addImageAndVoucher(urlImg: Uri, voucher: Voucher, callback: (b: Boolean) -> Unit) {
        var storageRef = FirebaseStorage.getInstance().reference.child("Vouchers")
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        storageRef.putFile(urlImg).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    addVoucher(voucher, uri) { value ->
                        callback(value)
                    }
                }
            } else {
                Log.e(TAG, "Upload image faild: ${task.exception?.message}")
            }
        }
    }

    fun addVoucher(voucher: Voucher, uri: Uri, callback: (b: Boolean) -> Unit) {
        val id = dbRealtime.push().key
        voucher.idVoucher = id!!
        voucher.img_url = uri.toString()
        dbRealtime.child(id).setValue(voucher)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { err ->
                callback(false)
                Log.e(TAG, "Không add đc imgage voucher: ${err.message}")
            }
    }


    fun selectVoucher(callback: (list: List<Voucher>) -> Unit) {
        val query = if (lastKey == null) {
            dbRealtime.orderByKey().limitToFirst(10)
        } else {
            dbRealtime.orderByKey().startAfter(lastKey).limitToFirst(10)
        }
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Voucher>()
                for (value in snapshot.children) {
                    lastKey = value.key
                    val voucher = value.getValue(Voucher::class.java)
                    if (voucher != null && (System.currentTimeMillis() - voucher.startTime!!) >= 0
                        && (System.currentTimeMillis() - voucher.endTime!!) < 0
                    ) {
                        list.add(voucher)
                    }
                }
                Log.w(TAG, "Số lượng: ${list.size}")
                callback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Lỗi truy vấn hóa đơn chờ xác nhận: ${error.message}")
                callback(emptyList())
            }
        })
    }
}