package com.developer.cory.FragmentLayout

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.Order
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Service.OrdersService
import com.developer.cory.databinding.FragmentLichSuMuaHangBinding


class LichSuMuaHangFragment : Fragment() {
   private lateinit var _binding:FragmentLichSuMuaHangBinding
    private val binding get() = _binding
    private val ordersService = OrdersService()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private lateinit var adapter: RvPurchaseHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLichSuMuaHangBinding.inflate(inflater,container,false)
        initView()
        return binding.root
    }

    private fun initView() {
        adapter = RvPurchaseHistoryAdapter(object : ClickObjectInterface<Order> {
            override fun onClickListener(t: Order) {

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
            ordersService.getFirstListPurchasedOrder { list->
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
            ordersService.loadNextListPurchasedOrders {
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