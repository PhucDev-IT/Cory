package com.developer.cory.Service


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class Product_Service(private val db:FirebaseFirestore)  {

    val maxSize:Long = 20
    private var lastIdProduct:DocumentSnapshot?=null

    //Lấy ngẫu nhiên số sản phẩm

    fun selectFirstPage(onDataLoaded: (List<Product>) -> Unit) {
        db.collection("Products").limit(maxSize)
            .get()
            .addOnSuccessListener { result ->
                val listProduct = mutableListOf<Product>()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    listProduct.add(product)
                }
                onDataLoaded(listProduct)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: .", exception)
            }
    }

    fun getNextPage(onDataLoaded: (List<Product>) -> Unit){
        db.collection("Products").startAfter(lastIdProduct).limit(maxSize)
            .get()
            .addOnSuccessListener { result ->
                val listProduct = mutableListOf<Product>()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    listProduct.add(product)
                }
                onDataLoaded(listProduct)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: .", exception)
            }
    }


    fun getDataByCategory(idCategory:String,callback:(List<Product>)->Unit){
        var list = mutableListOf<Product>()
        db.collection("Products").whereEqualTo("idCategory",idCategory)
            .get()
            .addOnSuccessListener {documents ->
                for(document in documents ){
                    var product = document.toObject(Product::class.java)
                    product.id = document.id
                    list.add(product)
                }
                callback(list)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: .", exception)
            }
    }


}