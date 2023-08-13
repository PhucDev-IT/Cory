package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.util.Log
import com.developer.cory.Model.Voucher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VoucherService {
    private val dbRealtime = FirebaseDatabase.getInstance().getReference("Vouchers")

    private var lastKey:String?=null

    fun selectVoucher(callback: (list: List<Voucher>) -> Unit) {
        val query = if (lastKey == null) {
            dbRealtime.orderByKey().limitToFirst(5)
        } else {
            dbRealtime.orderByKey().startAfter(lastKey).limitToFirst(5)
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