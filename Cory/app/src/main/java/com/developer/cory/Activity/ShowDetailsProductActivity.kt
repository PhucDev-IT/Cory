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
import com.developer.cory.R
import com.developer.cory.databinding.ActivityShowDetailsProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.Locale


class ShowDetailsProductActivity : AppCompatActivity() {
    val lc = Locale("vi", "VN")
    val numbf = NumberFormat.getCurrencyInstance(lc)

    private lateinit var binding: ActivityShowDetailsProductBinding
    private var numberBuyProduct: Int = 1
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

        binding.tvPrice.text = numbf.format(totalMoney)
        binding.btnAddProduct.text = (resources.getString(
            R.string.default_btn_add_to_cart,
           numbf.format(totalMoney)
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

                    binding.tvPrice.text = numbf.format(priceOfSize)
                    totalMoney = (numberBuyProduct * priceOfSize) + sumMoneySideDishes
                    binding.btnAddProduct.text = (resources.getString(
                        R.string.default_btn_add_to_cart,
                        numbf.format(totalMoney)
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
                        numbf.format(totalMoney)
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
            isExistProductInCart()
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
            (resources.getString(R.string.default_btn_add_to_cart, numbf.format(totalMoney)))


    }


    //Kiểm tra sản phẩm đã tồn tại trong giỏ hàng?
    private fun isExistProductInCart() {

        val cart = CartModel(numberBuyProduct, isChooseClassify, lsChooseSideDishes)
        dbRef.collection("Cart").document("Mv063f04SEUJxkSqPd2g").collection("ItemsCart")
                .document("rhAm1yTrREAOkWI3NsTJ")
            .set(cart, SetOptions.merge())

//        val cartItem =
//            dbRef.collection("Cart").document("Mv063f04SEUJxkSqPd2g").collection("ItemsCart")
//                .document("rhAm1yTrREAOkWI3NsTJ")
//        cartItem.get().addOnCompleteListener { taskId ->
//            if (taskId.isSuccessful) {
//                val doucment = taskId.result
//                if (doucment.exists()) {
//                    val cart = CartModel(numberBuyProduct, isChooseClassify, lsChooseSideDishes)
//                    dbRef.collection("Cart").document("Mv063f04SEUJxkSqPd2g").collection("ItemsCart")
//                        .document("rhAm1yTrREAOkWI3NsTJ")
//                        .set(cart)
//                } else {
//                    val cart = CartModel(numberBuyProduct, isChooseClassify, lsChooseSideDishes)
//
//                    cartItem.set(cart)
//                        .addOnSuccessListener {
//                            Log.d("TAG", "Đã tạo mới document cho sản phẩm thành công")
//                        }
//                        .addOnFailureListener { error ->
//                            Log.e("TAG", "Lỗi khi tạo mới document cho sản phẩm", error)
//                        }
//                }
//            } else {
//                Log.e("TAG", "Lỗi khi kiểm tra sự tồn tại của document", taskId.exception)
//            }
//        }
    }


    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    @Override
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}