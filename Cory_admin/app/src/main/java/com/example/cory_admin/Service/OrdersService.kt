package com.example.cory_admin.Service

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.Model.Order
import com.example.cory_admin.Model.Temp

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class OrdersService {
    private val pageSize:Int = 10
    private var database: DatabaseReference = Temp.user?.id?.let {
        FirebaseDatabase.getInstance().getReference("Orders").child(
            it
        )
    }!!



    var lastHoaDonKey: String? = null


    fun getValues(key:String):Query{
       // if(key == ""){
            return database.startAfter(key).orderByKey().equalTo("status", EnumOrder.CHOXACNHAN.name).limitToFirst(10)
        //}
        //return database.equalTo("status",EnumOrder.CHOXACNHAN.name).orderByKey().startAfter(key).limitToFirst(8)
    }


    fun donHangChoXacNhan(pageNumber: Int,lastKey:String?, callback: (List<Order>, keyLast:String?) -> Unit) {

        Log.d(TAG," Key là: $lastKey")
        val list = mutableListOf<Order>()

        database.orderByKey().startAt("").equalTo("status", EnumOrder.CHOXACNHAN.name).limitToFirst(10)
            .get().addOnSuccessListener { doucments->
                for(document in doucments.children){
                    val order = document.getValue(Order::class.java)
                    list.add(order!!)
                    lastHoaDonKey = document.key
                }
                callback(list,lastHoaDonKey)
            }
            .addOnFailureListener {e->
                Log.d(TAG," Không lấy đươc hóa đơn: ${e.message}")
            }
    }

    fun dangGiaoHang(callback: (List<Order>)->Unit){

        database.orderByChild("status").equalTo(EnumOrder.DANGGIAOHANG.name)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOrders = mutableListOf<Order>()
                    for(value in snapshot.children){
                        val order = value.getValue(Order::class.java)
                        if (order != null) {
                            listOrders.add(order)
                        }
                    }
                    callback(listOrders)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG,"Lỗi truy vấn hóa đơn chờ xác nhận: ${error.message}")
                    callback(emptyList())
                }
            })
    }

    fun lichSuMuaHang(callback: (List<Order>)->Unit){

        database.orderByChild("status").equalTo(EnumOrder.GIAOHANGTHANHCONG.name)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOrders = mutableListOf<Order>()
                    for(value in snapshot.children){
                        val order = value.getValue(Order::class.java)
                        if (order != null) {
                            listOrders.add(order)
                        }
                    }
                    callback(listOrders)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG,"Lỗi truy vấn hóa đơn chờ xác nhận: ${error.message}")
                    callback(emptyList())
                }
            })
    }

    fun huyDonHang(idOrder:String,callback:(Boolean)->Boolean){

        database.child(idOrder).child("status").setValue(EnumOrder.HUYDONHANG.name)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {e->
                callback(false)
                Log.e(TAG,"Có lỗi xóa hóa đơn: ${e.message} ")
            }
    }
}