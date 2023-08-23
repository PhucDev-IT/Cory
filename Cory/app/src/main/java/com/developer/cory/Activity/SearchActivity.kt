package com.developer.cory.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvCategoryAdapter
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Service.CategoryService
import com.developer.cory.Service.Product_Service
import com.developer.cory.ViewModel.SearchActivityViewModel
import com.developer.cory.databinding.ActivitySearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var adapter: RvProductInSearchApdapter
    private val db = Firebase.firestore
    private val productService = Product_Service(db)
    private var idCategory: String = ""

    private lateinit var sharedViewModel: SearchActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.layoutManager = linearLayoutManager

        adapter = RvProductInSearchApdapter(emptyList(), object : ClickObjectInterface<Product> {
            override fun onClickListener(product:Product) {
                val intent = Intent(this@SearchActivity, ShowDetailsProductActivity::class.java)
               intent.putExtra("product",product)
                startActivity(intent)
            }
        })
        binding.rvProducts.adapter = adapter


        binding.rvProducts.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
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
        handleEvent()
    }

    private fun initView() {
        initCategory()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstPageProduct() {
        binding.swipRefresh.isRefreshing = true
        Handler().postDelayed({
            productService.getDataByCategory(idCategory) { list ->
                if (list.isNotEmpty()) {
                    binding.lnHaveNotProduct.visibility = View.GONE
                    binding.rvProducts.visibility = View.VISIBLE
                    adapter.setData(list)
                    adapter.notifyDataSetChanged()

                } else {
                    binding.rvProducts.visibility = View.GONE
                    binding.lnHaveNotProduct.visibility = View.VISIBLE

                }
            }
            binding.swipRefresh.isRefreshing = false
        }, 1500)



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadNextProduct() {
        getValuesProduct()
        sharedViewModel.listProduct.observe(this) { list ->
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

    @SuppressLint("SuspiciousIndentation")
    private fun initCategory() {
        val pos = intent.getIntExtra("pos", 0)
        getDataCategory(pos)

    }

    private fun getDataCategory(pos: Int) {
        CategoryService(db).selectAllData { listCategory ->
            val adapter = RvCategoryAdapter(listCategory, object : RvInterface {
                override fun onClickListener(pos: Int) {
                    binding.tvTitleCategory.text = listCategory[pos].nameCategory
                    idCategory = listCategory[pos].id.toString()
                    getFirstPageProduct()
                }
            }, pos)
            binding.rvCategory.adapter = adapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun handleEvent() {
        binding.swipRefresh.setOnRefreshListener {
           getFirstPageProduct()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}