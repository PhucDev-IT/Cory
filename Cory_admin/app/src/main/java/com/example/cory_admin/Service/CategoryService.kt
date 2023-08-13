package com.example.cory_admin.Service

import android.content.ContentValues

import android.util.Log

import com.example.cory_admin.Model.Category
import com.google.firebase.firestore.FirebaseFirestore



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