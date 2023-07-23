package com.developer.cory.Service


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Product_Service(private val db:FirebaseFirestore)  {

    fun selectData(onDataLoaded: (List<Product>) -> Unit) {
        db.collection("Products")
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


}