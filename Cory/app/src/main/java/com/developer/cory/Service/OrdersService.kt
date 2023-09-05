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
    private val ORDER_CANCELED: String = "OrdersCanceled"


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

    fun loadNextListChoXacNhan( callback: (list: LiveData<List<Order>>?) -> Unit) {
        var query: Query = db.document(STATUS_ORDERS)
            .collection("ItemsOrder")
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .startAfter(lastHoaDonKey)
            .whereEqualTo("status", EnumOrder.CHOXACNHAN.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)


        query.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                error?.let {
                    Log.e(TAG, "Lỗi: ${it.message}")
                    callback(null)
                    return@let
                }

                if (value != null && value.size() > 0) {
                    lastHoaDonKey = value.documents[value.size() - 1]
                    val list = mutableListOf<Order>()
                    var lst = MutableLiveData<List<Order>>()
                    for (snap in value) {
                        val order = snap.toObject(Order::class.java)
                        if (order.status != EnumOrder.CHOXACNHAN.name)
                            continue
                        order.idOrder = snap.id
                        list.add(order)
                        Log.d(TAG, "Key: $order")
                    }
                    lst.value = list
                    callback(lst)
                }else{
                    callback(null)
                }
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    fun getFirstChoXacNhan( callback: (list: LiveData<List<Order>>?) -> Unit) {
        var query: Query = db.document(STATUS_ORDERS)
            .collection("ItemsOrder")
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .whereEqualTo("status", EnumOrder.CHOXACNHAN.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)

        query.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                error?.let {
                    Log.e(TAG, "Lỗi: ${it.message}")
                    callback(null)
                    return@let
                }

                if(value!=null && value.size()>0){
                    lastHoaDonKey = value.documents[value.size() - 1]
                    val list = mutableListOf<Order>()
                    var lst = MutableLiveData<List<Order>>()
                    for (snap in value) {
                        val order = snap.toObject(Order::class.java)
                        if (order.status != EnumOrder.CHOXACNHAN.name)
                            continue
                        order.idOrder = snap.id
                        list.add(order)
                        Log.d(TAG, "Key: $order")
                    }
                    lst.value = list
                    callback(lst)
                }else
                {
                    callback(null)
                }
            }
        })

    }

    fun getFirstListPurchasedOrder(callback: (list: List<Order>) -> Unit) {
        db.document(PURCHASED_HISTORY).collection(Temp.user?.id!!)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .limit(maxSize)
            .get().addOnSuccessListener { snapshotDocuments ->
                if (!snapshotDocuments.isEmpty) {
                    lastHoaDonKey = snapshotDocuments.documents[snapshotDocuments.size() - 1]

                    val list = mutableListOf<Order>()
                    for (snap in snapshotDocuments) {
                        var order = snap.toObject(Order::class.java)
                        order.idOrder = snap.id
                        list.add(order)
                    }
                    callback(list)
                }else{
                    callback(emptyList())
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }

    fun loadNextListPurchasedOrders(callback: (list: List<Order>) -> Unit){
        db.document(PURCHASED_HISTORY).collection(Temp.user?.id!!)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .startAfter(lastHoaDonKey)
            .limit(maxSize)
            .get().addOnSuccessListener { snapshotDocuments ->
                if (!snapshotDocuments.isEmpty) {
                    lastHoaDonKey = snapshotDocuments.documents[snapshotDocuments.size() - 1]

                    val list = mutableListOf<Order>()
                    for (snap in snapshotDocuments) {
                        var order = snap.toObject(Order::class.java)
                        order.idOrder = snap.id
                        list.add(order)
                    }
                    callback(list)
                }else{
                    callback(emptyList())
                }
            }
            .addOnFailureListener { err ->
                Log.e(TAG, "Có lỗi xảy ra khi load order: ${err.message}")
                callback(emptyList())
            }
    }



    fun huyDonHang(order:Order, callback: (b: Boolean) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder").document(order.idOrder!!)
            .delete()
            .addOnFailureListener {
                Log.e(TAG,"ooixix: ${it.message}")
            }
        db.document(ORDER_CANCELED).collection(Temp.user?.id!!).document(order.idOrder!!).set(order)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
                Log.e(TAG,"ooixix: ${it.message}")
            }

    }

    //Lây danh sách đơn hàng đã hủy
    fun getFirstListOrderCanceled(callback: (lst: List<Order>) -> Unit) {
        db.document(ORDER_CANCELED).collection(Temp.user?.id!!)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .whereEqualTo("status", EnumOrder.HUYDONHANG.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)
            .get()
            .addOnSuccessListener { query ->
                if(!query.isEmpty && query.size()>0){
                    lastHoaDonKey = query.documents[query.size() - 1]
                    val list = mutableListOf<Order>()
                    for (value in query) {
                        var order = value.toObject(Order::class.java)
                        order.idOrder = value.id
                        list.add(order)
                    }
                    callback(list)
                }else{
                    callback(emptyList())
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Lỗi: ${it.message}")
                callback(emptyList())
            }
    }

    fun loadNextListOrderCanceled(callback: (lst: List<Order>) -> Unit) {
        db.document(ORDER_CANCELED).collection(Temp.user?.id!!)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .startAfter(lastHoaDonKey)
            .whereEqualTo("status", EnumOrder.HUYDONHANG.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)
            .get()
            .addOnSuccessListener { query ->
                if(!query.isEmpty && query.size()>0){
                    lastHoaDonKey = query.documents[query.size() - 1]
                    val list = mutableListOf<Order>()
                    for (value in query) {
                        var order = value.toObject(Order::class.java)
                        order.idOrder = value.id
                        list.add(order)
                    }
                    callback(list)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Lỗi: ${it.message}")
            }
    }

    // --------- LẤY LIST ĐƠN HÀNG DANG ĐƯỢC GIAO -----------------
    fun getFirstListOrderTransporting(callback: (lst: List<Order>) -> Unit) {
        db.document(STATUS_ORDERS).collection("ItemsOrder")
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .whereEqualTo("status", EnumOrder.DANGGIAOHANG.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)
            .get()
            .addOnSuccessListener { query ->
                if(!query.isEmpty && query.size()>0){
                    lastHoaDonKey = query.documents[query.size() - 1]
                    val list = mutableListOf<Order>()
                    for (value in query) {
                        var order = value.toObject(Order::class.java)
                        order.idOrder = value.id
                        list.add(order)
                    }
                    callback(list)
                }else{
                    callback(emptyList())
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Lỗi: ${it.message}")
                callback(emptyList())
            }
    }

    fun loadNextListOrderTransporting(callback: (lst: List<Order>) -> Unit) {
        db.document(ORDER_CANCELED).collection("ItemsOrder")
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .startAfter(lastHoaDonKey)
            .whereEqualTo("status", EnumOrder.DANGGIAOHANG.name)
            .whereEqualTo("idUser", Temp.user?.id)
            .limit(maxSize)
            .get()
            .addOnSuccessListener { query ->
                if(!query.isEmpty && query.size()>0){
                    lastHoaDonKey = query.documents[query.size() - 1]
                    val list = mutableListOf<Order>()
                    for (value in query) {
                        var order = value.toObject(Order::class.java)
                        order.idOrder = value.id
                        list.add(order)
                    }
                    callback(list)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Lỗi: ${it.message}")
            }
    }


}