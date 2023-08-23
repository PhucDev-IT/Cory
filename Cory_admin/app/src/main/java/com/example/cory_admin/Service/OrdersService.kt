package com.example.cory_admin.Service

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.Model.Order
import com.example.cory_admin.Model.Temp
import com.example.cory_admin.Model.Voucher

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OrdersService {
    private val PURCHASED_HISTORY = "PurchasedHistory"
    private val STATUS_ORDERS: String = "StatusOrders"
    private val maxSize: Long = 5
    private val db = Firebase.firestore.collection("Orders")
    private var lastOrderKey: DocumentSnapshot? = null

    fun getAllOrderChoXacNhan(callback: (list: List<Order>) -> Unit) {
        var orderList = mutableListOf<Order>()

        db.document(STATUS_ORDERS).collection("ItemsOrder")
            .whereEqualTo("status", EnumOrder.CHOXACNHAN.name).orderBy("orderDate")
            .startAfter(lastOrderKey).limit(maxSize)
            .get().addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.isEmpty) {
                    lastOrderKey = documentSnapshot.documents[documentSnapshot.size() - 1]

                    for (document in documentSnapshot) {
                        var order = document.toObject(Order::class.java)
                        order.idOrder = document.id
                        orderList.add(order)
                    }

                    callback(orderList)
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }


    fun getAllOrderDangChoXuLy(callback: (list: List<Order>) -> Unit) {
        var orderList = mutableListOf<Order>()

        db.document(STATUS_ORDERS).collection("ItemsOrder")
            .whereEqualTo("status", EnumOrder.DANGGIAOHANG.name).orderBy("orderDate")
            .startAfter(lastOrderKey).limit(maxSize)
            .get().addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.isEmpty) {
                    lastOrderKey = documentSnapshot.documents[documentSnapshot.size() - 1]

                    for (document in documentSnapshot) {
                        var order = document.toObject(Order::class.java)
                        order.idOrder = document.id
                        orderList.add(order)
                    }
                    callback(orderList)
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }

    fun xacNhanDonHang(idDoucment: String, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").document(idDoucment)
            .update("status", EnumOrder.DANGGIAOHANG.name).addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun huyDonHang(idDoucment: String, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").document(idDoucment)
            .update("status", EnumOrder.HUYDONHANG.name).addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun removeOrder(idDoucment: String, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").document(idDoucment).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(false)
            }
    }

    //Sau khi giao hàng thì chuyển từ order ở trag thái đang mua sang collection đã mua hàng
    fun convertToPurchasedHistory(order: Order, callback: (b: Boolean) -> Unit) {
        order.idUser?.let {
            removeOrder(it) { b ->
                if (b) {
                    db.document(PURCHASED_HISTORY).collection(it).add(order).addOnSuccessListener {
                        callback(true)
                    }.addOnFailureListener { err ->
                        Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                        callback(false)
                    }
                }else{
                    callback(false)
                }
            }
        }
    }

    fun paginationReal(callback: (list: List<Order>) -> Unit) {
        val dbef = FirebaseDatabase.getInstance().getReference("Orders")
        val query = dbef
            .orderByValue()
            .equalTo("status", EnumOrder.CHOXACNHAN.name)
            .startAfter("-Nb4l5EX0x9mx32J_X6r") // Sử dụng startAfter() nếu có lastOrderKey
            .limitToFirst(6)

        query.get()
            .addOnSuccessListener { snapShot ->
                var list = mutableListOf<Order>()
                for (child in snapShot.children) {
                    val orders = child.getValue(Order::class.java)
                    list.add(orders!!)
                    Log.d(TAG, "Key: $orders")
                }
                callback(list)
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "LỖI: ${error.message}")
            }
    }


}