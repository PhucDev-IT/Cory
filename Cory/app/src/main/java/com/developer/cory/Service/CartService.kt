package com.developer.cory.Service

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvItemCartAdapter
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Product
import com.developer.cory.Model.Temp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartService {
    private var db = Firebase.firestore

    fun selectCartByID(idUser: String, callback: (list: List<CartModel>) -> Unit) {
        var listCart = mutableListOf<CartModel>()

        db.collection("Cart").document(idUser).collection("ItemsCart")
            .get().addOnSuccessListener { querySnapshot ->

                for (doucment in querySnapshot.documents) {
                    var idProduct = doucment.id.trim()
                    val cartData = doucment.data
                    var cartM: CartModel? = null

                    if (cartData != null) {
                        val classify = cartData["classify"] as String
                        val sideDishes = cartData["sideDishes"] as ArrayList<String>
                        val quantity = cartData["quantity"] as Long
                        cartM = CartModel(quantity, classify, sideDishes)

                    }
                    db.collection("Products").document(idProduct)
                        .get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val product = documentSnapshot.toObject(Product::class.java)

                                if (cartM != null) {
                                    cartM.product = product
                                    listCart.add(cartM)
                                }
                            } else {
                                Log.d(TAG, "Không có dữ liệu")
                            }

                            Log.d(TAG, "Values: ${listCart.size}")
                            callback(listCart)
                        }.addOnFailureListener { exception ->
                            Log.d(TAG, "Có lỗi xảy ra: ", exception)
                        }

                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Có lỗi xảy ra: ", exception)
                callback(emptyList())
            }
    }


    //xOA SẢN PHẨM SAU KHI MUA
    fun removeCart(list: List<CartModel>) {
        for (cartM in list) {
            cartM.product?.id?.let {
                Temp.user?.id?.let { it1 ->
                    db.collection("Cart").document(it1).collection("ItemsCart").document(it)
                        .delete()
                        .addOnFailureListener { e ->
                            e.message?.let { it2 -> Log.e("Không thể xóa cart: ", it2) }
                        }
                }
            }
        }
    }


    fun updateCart(idUser: String, cart: CartModel) {
        db.collection("Cart").document(idUser).collection("ItemsCart").document(cart.product?.id!!)
            .set(cart, SetOptions.merge())
            .addOnCompleteListener {
            }.addOnFailureListener { E ->
                Log.e(TAG, "Có lỗi: ", E)

            }
    }
}