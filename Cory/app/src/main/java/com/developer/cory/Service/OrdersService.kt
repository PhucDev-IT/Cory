package com.developer.cory.Service

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.developer.cory.Model.Order
import com.developer.cory.Model.Temp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrdersService {

    private lateinit var database: DatabaseReference

    private fun getConnect(){
        database = Temp.user?.id?.let {
            FirebaseDatabase.getInstance().getReference("Orders").child(it)
        }!!
    }

    fun donHangChoXacNhan(callback: (List<Order>)->Unit){
        getConnect()
        database.orderByChild("status").equalTo("Chờ xác nhận")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOrders = mutableListOf<Order>()
                    for(value in snapshot.children){
                        val order = value.getValue(Order::class.java)
                        if (order != null) {
                            listOrders.add(order)
                        }
                        Log.w(TAG,"$order")
                    }
                    callback(listOrders)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}