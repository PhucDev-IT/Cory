package com.developer.cory.Activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvCategoryAdapter
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.databinding.ActivitySearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var listCategory = mutableListOf<Category>()
    private lateinit var listProducts:MutableList<Product>
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        initCategory()
        listProducts = mutableListOf()


        for(i in 1..10){
            var product = Product()
            product.name = "Sản phẩm 0$i"
            product.price = i*73243.2
            product.description = "Trong layer-list này không có thuộc tính elevation. Thuộc tính elevation của view được sử dụng để tạo hiệu ứng đổ bóng trên các view và nó chỉ có thể được sử dụng với các view thông thường như FrameLayout, LinearLayout, RelativeLayout, vv. Các item trong layer-list chỉ định cách thức hiển thị các hình dạng và màu sắc, chúng không phải là các view thông thường và không hỗ trợ các thuộc tính như elevation"
            listProducts.add(product)
        }

        val adapter1 = RvProductInSearchApdapter(listProducts,object : RvInterface {
            override fun onClickListener(pos: Int) {
                val intent = Intent(this@SearchActivity, ShowDetailsProductActivity::class.java)
                intent.putExtra("product",listProducts[pos])
                startActivity(intent)
            }
        })

        binding.rvProducts.adapter =adapter1
        binding.rvProducts.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }

    private fun initCategory(){
        val idCategory = intent.getStringExtra("idCategory")
        val pos = intent.getIntExtra("pos",0)
        if(listCategory.size <= 0){

            getDataCategory(pos)
        }
    }

    private fun getDataCategory(pos:Int) {
        db.collection("Category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    listCategory.add(category)
                }
                val adapter = RvCategoryAdapter(listCategory,object : RvInterface {
                    override fun onClickListener(pos: Int) {
                        binding.tvTitleCategory.text = listCategory[pos].nameCategory
                    }
                },pos)

                binding.rvCategory.adapter = adapter
                binding.rvCategory.layoutManager =
                    LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)


            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Lỗi: .", exception)
                Toast.makeText(this, "Lỗi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}