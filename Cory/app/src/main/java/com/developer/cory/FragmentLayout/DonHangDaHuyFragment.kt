package com.developer.cory.FragmentLayout

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOrdersAdapter
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.R
import com.developer.cory.Service.OrdersService
import com.developer.cory.ViewModel.PurchaseHistoryViewModel
import com.developer.cory.databinding.FragmentDonHangDaHuyBinding


class DonHangDaHuyFragment : Fragment() {
    private lateinit var _binding: FragmentDonHangDaHuyBinding
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
        _binding = FragmentDonHangDaHuyBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        adapter = RvPurchaseHistoryAdapter(object : ClickObjectInterface<Order> {
            override fun onClickListener(t: Order) {

            }
        })


        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrder.adapter = adapter
        binding.rvOrder.layoutManager = linearLayoutManager

        binding.rvOrder.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
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
            ordersService.getFirstListOrderCanceled { list->
                if (list.isNotEmpty()) {
                    binding.rvOrder.visibility = View.VISIBLE
                    binding.lnHaveNotOrders.visibility = View.GONE
                    adapter.setData(list)

                } else {
                    binding.lnHaveNotOrders.visibility = View.VISIBLE
                    binding.rvOrder.visibility = View.GONE
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
            ordersService.loadNextListOrderCanceled() {
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