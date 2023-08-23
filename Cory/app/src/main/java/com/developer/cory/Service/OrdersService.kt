package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.util.Log

import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.Temp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrdersService {

    private val PURCHASED_HISTORY = "PurchasedHistory"
    private val STATUS_ORDERS: String = "StatusOrders"


    private val db = Firebase.firestore.collection("Orders")
    private val maxSize: Long = 5

    private var lastHoaDonKey: DocumentSnapshot? = null


    fun addOrder(order: Order, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").add(order)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi thêm order: ${err.message}")
                callback(false)
            }
    }

    fun loadNextPage(statusOrder:String,callback: (list: List<Order>) -> Unit) {
        var query: Query = db.document(STATUS_ORDERS)
            .collection("ItemsOrder")
            .orderBy("idOrder")
            .startAfter(lastHoaDonKey!!.id)
            .whereEqualTo("status",statusOrder)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)


        query.get()
            .addOnSuccessListener { snapshotDocuments ->
                if (!snapshotDocuments.isEmpty) {
                    lastHoaDonKey = snapshotDocuments.documents[snapshotDocuments.size() - 1]
                    val list = mutableListOf<Order>()
                    for (snap in snapshotDocuments) {
                        val order = snap.toObject(Order::class.java)
                        order.idOrder = snap.id
                        list.add(order)
                    }
                    callback(list)
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
            }
        callback(emptyList())
    }

    fun getFirsPage(statusOrder:String,callback: (list: List<Order>) -> Unit) {
        var query: Query = db.document(STATUS_ORDERS)
            .collection("ItemsOrder")
            .orderBy("idOrder")
            .whereEqualTo("status", statusOrder)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)

        query.get()
            .addOnSuccessListener { snapshotDocuments ->
                if (!snapshotDocuments.isEmpty) {
                    lastHoaDonKey = snapshotDocuments.documents[snapshotDocuments.size() - 1]
                    val list = mutableListOf<Order>()
                    for (snap in snapshotDocuments) {
                        val order = snap.toObject(Order::class.java)
                        order.idOrder = snap.id
                        list.add(order)
                        Log.d(TAG, "Key: $order")
                    }
                    callback(list)
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }

    fun donHangDaMua(callback: (list: List<Order>) -> Unit) {
        db.document(PURCHASED_HISTORY).collection(Temp.user?.id!!).orderBy("orderDate")
            .startAfter(lastHoaDonKey)
            .limit(maxSize)
            .get().addOnSuccessListener { snapshotDocuments ->
                if (!snapshotDocuments.isEmpty) {
                    lastHoaDonKey = snapshotDocuments.documents[snapshotDocuments.size() - 1]
                    Log.d(TAG, "Key: $lastHoaDonKey")
                    val list = mutableListOf<Order>()
                    for (snap in snapshotDocuments) {
                        var order = snap.toObject(Order::class.java)
                        order.idOrder = snap.id
                        list.add(order)
                    }
                    callback(list)
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }


}