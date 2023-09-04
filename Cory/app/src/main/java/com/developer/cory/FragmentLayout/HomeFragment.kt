package com.developer.cory.FragmentLayout


import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.developer.cory.Activity.RvProductApdapter
import com.developer.cory.Activity.SearchActivity
import com.developer.cory.Activity.ShowDetailsProductActivity
import com.developer.cory.Adapter.RvCategoryHome
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.R
import com.developer.cory.Service.CategoryService
import com.developer.cory.Service.Product_Service
import com.developer.cory.database.ProductDatabase
import com.developer.cory.databinding.FragmentHomeBinding
import com.developer.cory.databinding.ShimmerLayoutHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private val db = Firebase.firestore
    private val productService = Product_Service(db)
    private val categoryService = CategoryService(db)

    private lateinit var adapterSuggest: RvProductApdapter

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initSlider()
        initCategory()
        initProduct()
       // loadListLikedProduct()

        registerEventClick()
        return binding.root
    }

    private fun initProduct() {

        adapterSuggest = RvProductApdapter(emptyList(), object : ClickObjectInterface<Product> {
            override fun onClickListener(t: Product) {
                val intent = Intent(context, ShowDetailsProductActivity::class.java)
                intent.putExtra("product", t)
                startActivity(intent)
            }
        })

        val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 2)
        binding.rvGoiY.adapter = adapterSuggest
        binding.rvGoiY.layoutManager = linearLayoutManager

        binding.rvGoiY.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItem() {
                loadNextProduct()
            }

            override fun isLoading(): Boolean {
               return isLoading
            }

            override fun isLastPage(): Boolean {
               return  isLastPage
            }
        })

        getFirstListProductSuggestToday()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstListProductSuggestToday() {

        productService.selectFirstPage { list ->
            adapterSuggest.setData(list)
            adapterSuggest.notifyDataSetChanged()

            if (list.size < productService.maxSize) {
                isLastPage = true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadNextProduct() {
        isLoading = true
        Handler().postDelayed({
            productService.getNextPage { list ->
                adapterSuggest.setData(list)
                adapterSuggest.notifyDataSetChanged()

                if (list.size < productService.maxSize) {
                    isLastPage = true
                }
            }
            isLoading = false
        }, 1500)
    }

    private fun initSlider() {
        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(R.drawable.slider, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider3, ScaleTypes.CENTER_CROP))

        binding.imageSlider.setImageList(imageList)
    }

    private fun initCategory() {
        categoryService.selectAllData { listCategory ->
            val adapter = RvCategoryHome(listCategory, object : RvInterface {
                override fun onClickListener(pos: Int) {
                    val intent = Intent(context, SearchActivity::class.java)
                    intent.putExtra("idCategory", listCategory[pos].id)
                    intent.putExtra("pos", pos)

                    startActivity(intent)
                }
            })
            binding.rvCategory.adapter = adapter
            binding.rvCategory.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        }
    }


    private fun registerEventClick() {
        binding.swipRefresh.setOnRefreshListener {
            Handler().postDelayed({
                getFirstListProductSuggestToday()
                binding.swipRefresh.isRefreshing = false
            }, 2000)
        }

        binding.tvSearchProduct.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    //Phaan trang sản phẩm ưa thích

    private fun initLikedProduct(){

    }



}