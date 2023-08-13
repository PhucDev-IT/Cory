package com.example.cory_admin.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.RvProductAdapter
import com.example.cory_admin.Activity.AddProductActivity
import com.example.cory_admin.Adapter.RvCategoryAdapter
import com.example.cory_admin.Interface.RvInterface
import com.example.cory_admin.Model.PaginationScrollListener
import com.example.cory_admin.R
import com.example.cory_admin.Service.CategoryService
import com.example.cory_admin.Service.Product_Service
import com.example.cory_admin.databinding.FragmentProductsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProductFragment : Fragment(),OnClickListener {
   private lateinit var _binding:FragmentProductsBinding
    private val binding get() = _binding
    private val db = Firebase.firestore
    private lateinit var adapter: RvProductAdapter

    private var productService = Product_Service(db)

    private var isLoading:Boolean = false
    private var isLastPage:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater,container,false)


        initView()
        getCategory()
        handleClick()
        return binding.root
    }

    private fun initView(){
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.layoutManager = linearLayoutManager

        adapter = RvProductAdapter(emptyList(),object : RvInterface{
            override fun onClickListener(pos: Int) {

            }
        })

        binding.rvProducts.adapter = adapter

        // Xử lý phân trang khi cuộn
        binding.rvProducts.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItem() {
                isLoading = true
                binding.swipRefresh.isRefreshing = true
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })
    }

    private fun getCategory(){
        CategoryService(db).selectAllData { listCategory ->
            val adapter = RvCategoryAdapter(listCategory, object : RvInterface {
                override fun onClickListener(pos: Int) {
                    binding.tvTitleCategory.text = listCategory[pos].nameCategory
                    binding.swipRefresh.isRefreshing = true

                    // Get the product data after the category is clicked
                    getValuesProduct(listCategory[pos].id ?: "")
                }
            })
            binding.rvCategory.adapter = adapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getValuesProduct(idCate:String){
        productService.getDataByCategory(idCate){list->
            adapter.setData(list)
            adapter.notifyDataSetChanged()
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        }
    }


    private fun handleClick(){
        binding.fabAdd.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        when(view){
            binding.fabAdd ->{
                val intent = Intent(context,AddProductActivity::class.java)
                startActivity(intent)
            }
        }
    }

}