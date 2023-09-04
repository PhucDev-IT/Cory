package com.example.cory_admin.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProductFragment : Fragment(),OnClickListener {
    private lateinit var _binding:FragmentProductsBinding
    private val binding get() = _binding
    private val db = Firebase.firestore
    private lateinit var adapter: RvProductAdapter

    private var productService = Product_Service(db)
    private var idCategoryChoiced : String = "a"
    private var lastProductKey:DocumentSnapshot?=null
    private var isLoading:Boolean = false
    private var isLastPage:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater,container,false)


        initView()

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
                getNextPageProduct()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })

        getCategory()
    }

    private fun getCategory(){
        CategoryService(db).selectAllData { listCategory ->
            val adapter = RvCategoryAdapter(listCategory, object : RvInterface {
                override fun onClickListener(pos: Int) {
                    binding.tvTitleCategory.text = listCategory[pos].nameCategory
                    idCategoryChoiced = listCategory[pos].id.toString()
                    getFirstPageProduct()
                }
            },0)
            binding.rvCategory.adapter = adapter
            binding.rvCategory.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNextPageProduct(){
        isLoading = true
        binding.swipRefresh.isRefreshing = true

        Handler().postDelayed({
            productService.getNextPage(idCategoryChoiced,lastProductKey){list,lastIdProduct->
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                lastProductKey = lastIdProduct

                if(list.size < productService.maxSize){
                    isLastPage = true
                    Toast.makeText(context,"Hết sản phẩm", Toast.LENGTH_SHORT).show()
                }
            }
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        },1000)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstPageProduct(){
        isLoading = true
        binding.swipRefresh.isRefreshing = true

        Handler().postDelayed({
            productService.getFirstPage(idCategoryChoiced){list,lastIdProduct->
                lastProductKey = lastIdProduct

                if(list.isNotEmpty()){
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.lnNotAvailable.visibility = View.GONE
                    adapter.setData(list)
                    adapter.notifyDataSetChanged()

                    if(list.size < productService.maxSize){
                        isLastPage = true
                    }

                }else{
                    binding.lnNotAvailable.visibility = View.VISIBLE
                    binding.rvProducts.visibility = View.GONE
                    isLastPage = true
                }
            }
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        },1000)
    }

    private fun handleClick(){
        binding.fabAdd.setOnClickListener(this)

        binding.swipRefresh.setOnRefreshListener {
            getFirstPageProduct()
        }
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