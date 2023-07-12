package com.developer.cory.FragmentLayout

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.developer.cory.Model.Category
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.R
import com.developer.cory.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private val db = Firebase.firestore



    private lateinit var listProduct: MutableList<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initCategory()
        getData()

        swipeRefreshLayout = binding.swipRefresh

        binding.tvSearchProduct.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        initSlider()
        return binding.root
    }


    private fun getData() {
        listProduct = mutableListOf()
        db.collection("Products")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    listProduct.add(product)
                }
                val adapter = RvProductApdapter(listProduct, object : RvInterface {
                    override fun onClickListener(pos: Int) {
                        val intent = Intent(context, ShowDetailsProductActivity::class.java)
                        intent.putExtra("product", listProduct[pos])
                        startActivity(intent)
                    }
                })


                binding.rvDemo.adapter = adapter
                binding.rvDemo.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL, false
                )
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: .", exception)
                Toast.makeText(context, "Lỗi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initSlider() {
        val imageList = ArrayList<SlideModel>() // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(SlideModel(R.drawable.slider, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.slider3, ScaleTypes.CENTER_CROP))


        binding.imageSlider.setImageList(imageList)
    }

    private fun initCategory() {
        var listCategory = ArrayList<Category>()
        db.collection("Category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    listCategory.add(category)
                }
                val adapter = RvCategoryHome(listCategory,object : RvInterface {

                    override fun onClickListener(pos: Int) {
                        val intent = Intent(context,SearchActivity::class.java)
                        intent.putExtra("idCategory",listCategory[pos].id)
                        intent.putExtra("pos",pos)

                        startActivity(intent)
                    }
                })

                binding.rvCategory.adapter = adapter
                binding.rvCategory.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)


            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: .", exception)
                Toast.makeText(context, "Lỗi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onRefresh() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                swipeRefreshLayout.isRefreshing = false
            }
        }, 2000) // đợi 1 giây trước khi chạy Runnable
    }
}