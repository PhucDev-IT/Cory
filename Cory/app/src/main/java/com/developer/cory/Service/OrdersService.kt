package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.Temp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
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


    fun choXacNhan(callback: (list: List<Order>) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").orderBy("orderDate")
            .startAfter(lastHoaDonKey).whereEqualTo("status", EnumOrder.CHOXACNHAN.name)
            .whereEqualTo("idUser", Temp.user?.id)
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

    fun dangGiaoHang(callback: (list: List<Order>) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").orderBy("orderDate")
            .startAfter(lastHoaDonKey).whereEqualTo("status", EnumOrder.DANGGIAOHANG.name)
            .whereEqualTo("idUser", Temp.user?.id)
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