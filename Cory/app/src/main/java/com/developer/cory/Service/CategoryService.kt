package com.developer.cory.Service

import android.content.ContentValues

import android.util.Log
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.Category
import com.developer.cory.Model.Temp
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CategoryService(private val db:FirebaseFirestore) {

    fun selectAllData(callback: (List<Category> )->Unit){
        var listCategory = ArrayList<Category>()
        db.collection("Category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    listCategory.add(category)
                }
                callback(listCategory)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
                Log.e(ContentValues.TAG, "Lỗi: .", exception)
            }
    }


}