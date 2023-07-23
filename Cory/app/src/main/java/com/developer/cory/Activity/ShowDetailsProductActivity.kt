package com.developer.cory.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.developer.cory.Adapter.RvCheckBSideDishes
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.databinding.ActivityShowDetailsProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.Locale


class ShowDetailsProductActivity : AppCompatActivity() {


    private lateinit var binding: ActivityShowDetailsProductBinding
    private var numberBuyProduct: Long = 1
    private lateinit var product: Product
    private var totalMoney: Double = 0.0
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var lsChooseSideDishes: ArrayList<String>
    private var isChooseClassify: String = ""
    private var priceOfSize:Double = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = Firebase.firestore

        initView()
        handleEvent()
    }


    private fun initView() {
        product = intent.getSerializableExtra("product") as Product


        binding.tvNameProduct.text = product.name
        binding.tvDescription.text = product.description
        Glide.with(this).load(product.img_url).into(binding.imgProduct)
        totalMoney = product.price!!
        priceOfSize = product.price!!


        initClassify()
        initSideDishes()
    }

    //Init phân loại size
    private fun initClassify() {

        binding.tvPrice.text = FormatCurrency.numberFormat.format(totalMoney)
        binding.btnAddProduct.text = (resources.getString(
            R.string.default_btn_add_to_cart,
            FormatCurrency.numberFormat.format(totalMoney)
        ))

        val inflater = LayoutInflater.from(this)
        val radioButtonLayout = R.layout.viewholder_classify_product

        var index = 0
        product.listSize?.forEach { (key, value) ->
            val radioButton =
                inflater.inflate(radioButtonLayout, binding.radioGroup, false) as RadioButton

            radioButton.text = key
            radioButton.setOnCheckedChangeListener { buttonView, ischecked ->
                if (ischecked) {

                    isChooseClassify = buttonView.text.toString()   //Những sản phẩm mua thêm save firebase

                    priceOfSize = product.listSize!![buttonView.text.toString()] as Double

                    binding.tvPrice.text = FormatCurrency.numberFormat.format(priceOfSize)
                    totalMoney = (numberBuyProduct * priceOfSize) + sumMoneySideDishes
                    binding.btnAddProduct.text = (resources.getString(
                        R.string.default_btn_add_to_cart,
                        FormatCurrency.numberFormat.format(totalMoney)
                    ))

                }
            }
            binding.radioGroup.addView(radioButton)
            if (index == 0) {
                radioButton.isChecked = true
            }
            index++
        }
    }

    private var sumMoneySideDishes: Double = 0.0

    //Khởi tạo list món ăn phụ
    private fun initSideDishes() {

            lsChooseSideDishes = arrayListOf()

        val entries = product.sideDishes?.entries?.toList()
        val adapter = entries?.let {
            RvCheckBSideDishes(it, object : RvPriceInterface {
                override fun onClickListener(price: Double, pos: Int) {
                    sumMoneySideDishes += price
                    totalMoney += price
                    binding.btnAddProduct.text = (resources.getString(
                        R.string.default_btn_add_to_cart,
                        FormatCurrency.numberFormat.format(totalMoney)
                    ))
                    lsChooseSideDishes.add(entries[pos].key)
                }
            })
        }

        binding.rvCkbSideDishes.adapter = adapter
        binding.rvCkbSideDishes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun handleEvent() {
        binding.btnClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddProduct.setOnClickListener {
            addProductToCart()
        }

        binding.btnUp.setOnClickListener {
            UpDownNumberProduct(binding.btnUp)
        }
        binding.btnMinus.setOnClickListener {
            UpDownNumberProduct(binding.btnMinus)
        }

    }

    private fun UpDownNumberProduct(view: View) {
        if (view.id == R.id.btnUp) {
            numberBuyProduct++
        } else if (view.id == R.id.btnMinus && numberBuyProduct > 1) {
            numberBuyProduct--
        }

        binding.tvNumberBuyProduct.text = numberBuyProduct.toString()
        totalMoney  = priceOfSize * numberBuyProduct + sumMoneySideDishes

        binding.btnAddProduct.text =
            (resources.getString(R.string.default_btn_add_to_cart, FormatCurrency.numberFormat.format(totalMoney)))


    }


    //Thêm sản phẩm vào giỏ hàng
    private fun addProductToCart() {
        if(Temp.user==null){

            return
        }
        val idUser = Temp.user!!.id
        val cart = CartModel(numberBuyProduct, isChooseClassify, lsChooseSideDishes)

        product.id?.let {
            dbRef.collection("Cart").document(idUser!!).collection("ItemsCart")
                .document(it)
                .set(cart, SetOptions.merge())
                .addOnCompleteListener {
                    Toast.makeText(this,"Thêm thành công",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                }
        }

    }


    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    @Override
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}