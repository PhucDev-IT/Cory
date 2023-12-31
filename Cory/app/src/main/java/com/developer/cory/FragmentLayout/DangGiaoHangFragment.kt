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
        initView()
        return binding.root
    }


    private fun initView() {
        adapter = RvPurchaseHistoryAdapter(object : ClickObjectInterface<Order> {
            override fun onClickListener(t: Order) {
                val intent = Intent(context, PurchaseHistoryDetailsActivity::class.java)
                intent.putExtra("key_order",t)
                startActivity(intent)
            }
        })


        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrders.adapter = adapter
        binding.rvOrders.layoutManager = linearLayoutManager

        binding.rvOrders.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItem() {
                loadNextOrder()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })

        getFirstListOrder()
    }

    private fun getFirstListOrder() {
        isLoading = true
        binding.swipRefresh.isRefreshing = true
        Handler().postDelayed({
            ordersService.getFirstListOrderTransporting { list->
                if (list.isNotEmpty()) {
                    binding.rvOrders.visibility = View.VISIBLE
                    binding.lnHaveNotOrders.visibility = View.GONE
                    adapter.setData(list)

                } else {
                    binding.lnHaveNotOrders.visibility = View.VISIBLE
                    binding.rvOrders.visibility = View.GONE
                    isLastPage = true
                }
            }
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        }, 1500)
    }

    private fun loadNextOrder() {
        isLoading = true
        binding.swipRefresh.isRefreshing = true
        Handler().postDelayed({
            ordersService.loadNextListOrderTransporting {
                adapter.setData(it)

                if (it.isEmpty()) {
                    isLastPage = true
                }
            }
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        }, 1500)
    }


}