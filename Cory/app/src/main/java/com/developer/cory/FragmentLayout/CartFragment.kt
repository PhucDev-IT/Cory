package com.developer.cory.FragmentLayout

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvItemCartAdapter
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Product
import com.developer.cory.R
import com.developer.cory.databinding.FragmentCartBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {
   private  lateinit var _binding:FragmentCartBinding
    private val binding  get() = _binding
    private lateinit var listBuyProduct:MutableList<CartModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater,container,false)
        getData()
        return binding.root
    }

    private lateinit var listCart:MutableList<CartModel>
    private var totalMoney:Double = 0.0
    private fun getData()
    {
        val db = Firebase.firestore

        listBuyProduct = mutableListOf()
        listCart = mutableListOf()

        db.collection("Cart").document("l3vFMy0dOKaaxHfNcnV1").collection("ItemsCart")
            .get().addOnSuccessListener { querySnapshot  ->

                for(doucment in querySnapshot.documents){
                    var idProduct = doucment.id.trim()
                    val cartData = doucment.data
                    var cartM:CartModel?=null

                    if(cartData!=null){
                        val classify = cartData["classify"] as String
                       val sideDishes = cartData["sideDishes"] as ArrayList<String>
                       val quantity = cartData["quantity"] as Long
                        cartM = CartModel(quantity,classify,sideDishes)

                    }
                    db.collection("Products").document(idProduct)
                        .get().addOnSuccessListener {  documentSnapshot ->
                            if(documentSnapshot.exists()){
                               val product = documentSnapshot.toObject(Product::class.java)

                                if (cartM != null) {
                                    cartM.product = product
                                    listCart.add(cartM)

                                    val adapter = RvItemCartAdapter(listCart, object : RvPriceInterface{
                                        override fun onClickListener(price: Double, pos: Int) {
                                           
                                            totalMoney+=price
                                            binding.tvSumMoney.text = FormatCurrency.numberFormat.format(totalMoney)
                                        }
                                    })
                                    binding.rvItemCart.adapter = adapter
                                    binding.rvItemCart.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                                }
                            }else{
                               Log.d(TAG,"Không có dữ liệu")
                            }
                        }.addOnFailureListener { exception ->
                            Log.d(TAG, "Có lỗi xảy ra: ", exception)
                        }

                }
            }
    }
}