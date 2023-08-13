package com.example.cory_admin.Service


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast

import com.example.cory_admin.Model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Product_Service(private val db:FirebaseFirestore)  {



    private fun addImage(urlImg: Uri,callback: (uri:String) -> Unit){
        var storageRef = FirebaseStorage.getInstance().reference.child("Products")
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        storageRef.putFile(urlImg).addOnCompleteListener {task->
            if(task.isSuccessful){
                storageRef.downloadUrl.addOnSuccessListener { uri->
                    callback(uri.toString())
                }
            }else{
                Log.e(TAG,"Upload image faild: ${task.exception?.message}")
            }
        }
    }

     fun addProduct(url:Uri,product:Product,callback: (b:Boolean) -> Unit){
        addImage(url){uri->
            product.img_url = uri
            db.collection("Products").add(product).addOnSuccessListener {
                callback(true)
            }
                .addOnFailureListener{er->
                    callback(false)
                    Log.e(TAG,"Thêm product faild: ${er.message}")
                }
        }
    }



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


    fun getDataByCategory(idCategory:String,callback:(List<Product>)->Unit){
        db.collection("Products").whereEqualTo("idCategory",idCategory)
            .get()
            .addOnSuccessListener {documents ->
                var list = mutableListOf<Product>()
                for(document in documents ){
                    var product = document.toObject(Product::class.java)
                    Log.d(TAG,"Values: $product")
                    product.id = document.id
                    list.add(product)
                }
                callback(list)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }


}