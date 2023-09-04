package com.developer.cory.Service

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.Temp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase

class OrdersService {

    private val PURCHASED_HISTORY = "PurchasedHistory"
    private val STATUS_ORDERS: String = "StatusOrders"

    private val _ordersLiveData = MediatorLiveData<List<Order>>()
    val ordersLiveData: LiveData<List<Order>> get() = _ordersLiveData

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

    @SuppressLint("SuspiciousIndentation")
    fun getFirsPage(statusOrder:String, callback: (list: LiveData<List<Order>>?) -> Unit) {
        var query: Query = db.document(STATUS_ORDERS)
            .collection("ItemsOrder")
            .orderBy("idOrder")
            .whereEqualTo("status", statusOrder)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)

            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    error?.let {
                        Log.e(TAG,"Lỗi: ${it.message}")
                        callback(null)
                        return@let
                    }

                    value?.let {
                        lastHoaDonKey = value.documents[value.size() - 1]
                        val list = mutableListOf<Order>()
                        var lst = MutableLiveData<List<Order>>()
                        for (snap in value) {
                            val order = snap.toObject(Order::class.java)
                            if(order.status!=statusOrder)
                                continue
                            order.idOrder = snap.id
                            list.add(order)
                            Log.d(TAG, "Key: $order")
                        }
                        lst.value = list
                        callback(lst)
                    }
                }
            })

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

    fun huyDonHang(idDoucment: String, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").document(idDoucment)
            .update("status", EnumOrder.HUYDONHANG.name).addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun addOrderRel(order: Order, callback: (b: Boolean) -> Unit){
        var dbRef = FirebaseDatabase.getInstance().getReference("Orders")

        var idOrder = dbRef.push().key
        order.idOrder = idOrder

        dbRef.child(Temp.user?.id!!).child(idOrder!!).setValue(order).addOnSuccessListener {
            callback(true)
        }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi thêm order: ${err.message}")
                callback(false)
            }
    }


}