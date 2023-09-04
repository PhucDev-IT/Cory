package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.PurchaseHistoryDetailsActivity
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Service.OrdersService
import com.developer.cory.databinding.FragmentDangGiaoHangBinding

class DangGiaoHangFragment : Fragment() {
    private lateinit var _binding: FragmentDangGiaoHangBinding
    private val binding get() = _binding

    private val ordersService = OrdersService()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var listOrder = mutableListOf<Order>()
    private lateinit var adapter: RvPurchaseHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDangGiaoHangBinding.inflate(inflater,container,false)
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvDangGiaoHang.layoutManager = linearLayoutManager

        adapter = RvPurchaseHistoryAdapter(object : ClickObjectInterface<Order> {
            override fun onClickListener(t: Order) {
                val intent = Intent(context, PurchaseHistoryDetailsActivity::class.java)
                intent.putExtra("key_order",t)
                startActivity(intent)
            }
        })
        binding.rvDangGiaoHang.adapter = adapter

        binding.rvDangGiaoHang.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItem() {
                isLoading = true
                loadNext()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })

        getFirstPage()
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstPage() {

//        isLoading = true
//        Handler().postDelayed({
//            ordersService.getFirsPage(EnumOrder.DANGGIAOHANG.name) { list ->
//                adapter.setData(list)
//                adapter.notifyDataSetChanged()
//                if (list.isEmpty()) {
//                   // binding.lnChuaCoDonHang.visibility = View.VISIBLE
//                } else if (list.size < 5) {
//                    isLastPage = true
//                    Toast.makeText(context, "Hết dữ liệu", Toast.LENGTH_SHORT).show()
//                }
//            }
//            isLoading = false
//          //  binding.progressBarLoading.visibility = View.GONE
//        }, 1500)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNext() {
       // binding.progressBarLoading.visibility = View.VISIBLE

        Handler().postDelayed({
            ordersService.loadNextPage(EnumOrder.DANGGIAOHANG.name) { list ->
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    isLastPage = true
                }
            }

            isLoading = false
         //   binding.progressBarLoading.visibility = View.GONE
        }, 1500)
    }


}