package com.developer.cory.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvCategoryAdapter
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Service.CategoryService
import com.developer.cory.Service.Product_Service
import com.developer.cory.ViewModel.SearchActivityViewModel
import com.developer.cory.databinding.ActivitySearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var adapter: RvProductInSearchApdapter
    private val db = Firebase.firestore
    private val productService = Product_Service(db)
    private lateinit var idCategory: String

    private lateinit var sharedViewModel: SearchActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.layoutManager = linearLayoutManager

        adapter = RvProductInSearchApdapter(emptyList(), object : RvInterface {
            override fun onClickListener(pos: Int) {

            }
        })
        binding.rvProducts.adapter = adapter


        binding.rvProducts.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItem() {
                sharedViewModel.isLoading = true
                loadNextProduct()
            }

            override fun isLoading(): Boolean {
                return sharedViewModel.isLoading
            }

            override fun isLastPage(): Boolean {
                return sharedViewModel.isLastPage
            }
        })


        initView()
    }

    private fun initView() {
        initCategory()
        getFirstPageProduct()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstPageProduct() {
        productService.getDataByCategory(idCategory) { list ->
            adapter.setData(list)
            adapter.notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadNextProduct(){
        getValuesProduct()
        sharedViewModel.listProduct.observe(this){list->
           // sharedViewModel.isLoading = false
            adapter.setData(list)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getValuesProduct() {
        productService.getDataByCategory(idCategory) { list ->
            sharedViewModel.setListProduct(list)
        }
    }

    private fun initCategory() {
        idCategory = intent.getStringExtra("idCategory").toString()
        val pos = intent.getIntExtra("pos", 0)
//        val listCategory = intent.getSerializableExtra("listCate") as Array<Category>
//
//        if (listCategory.isEmpty()) {
          getDataCategory(pos)
//        }
    }

    private fun getDataCategory(pos: Int) {
        CategoryService(db).selectAllData { listCategory ->
            val adapter = RvCategoryAdapter(listCategory, object : RvInterface {
                override fun onClickListener(pos: Int) {
                    binding.tvTitleCategory.text = listCategory[pos].nameCategory
                }
            }, pos)
            idCategory = listCategory[0].id.toString()
            binding.rvCategory.adapter = adapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        }
    }


}