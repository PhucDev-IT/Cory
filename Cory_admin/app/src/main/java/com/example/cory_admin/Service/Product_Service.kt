package com.example.cory_admin.Service


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast

import com.example.cory_admin.Model.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Product_Service(private val db:FirebaseFirestore)  {


    val maxSize:Long = 10



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


    fun getFirstPage(idCategory:String,callback:(list:List<Product>,lastProductKey:DocumentSnapshot?)->Unit){

        db.collection("Products").whereEqualTo("idCategory",idCategory)
            .limit(maxSize).get()
            .addOnSuccessListener {snapshotDocument ->

                if(!snapshotDocument.isEmpty){
                    val lastIdProduct = snapshotDocument.documents[snapshotDocument.size()-1]

                    var list = mutableListOf<Product>()
                    for(document in snapshotDocument ){

                        var product = document.toObject(Product::class.java)
                        product.id = document.id
                        list.add(product)
                    }
                    callback(list,lastIdProduct)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        callback(emptyList(),null)
    }

    fun getNextPage(idCategory:String,lastIdProduct:DocumentSnapshot?,callback:(list:List<Product>,lastProductKey:DocumentSnapshot?)->Unit){
        db.collection("Products").whereEqualTo("idCategory",idCategory).orderBy("price")
            .startAfter(lastIdProduct)
            .limit(maxSize).get()
            .addOnSuccessListener {snapshotDocument ->

                if(!snapshotDocument.isEmpty){
                    val lastProductKey = snapshotDocument.documents[snapshotDocument.size()-1]

                    var list = mutableListOf<Product>()
                    for(document in snapshotDocument ){

                        var product = document.toObject(Product::class.java)
                        product.id = document.id
                        list.add(product)
                    }
                    callback(list,lastProductKey)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                callback(emptyList(),null)
            }
    }


}