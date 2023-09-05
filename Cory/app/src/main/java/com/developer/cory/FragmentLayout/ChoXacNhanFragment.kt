package com.developer.cory.FragmentLayout


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.PurchaseHistoryDetailsActivity
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Service.OrdersService
import com.developer.cory.ViewModel.PurchaseHistoryViewModel
import com.developer.cory.databinding.FragmentChoXacNhanBinding


class ChoXacNhanFragment : Fragment() {
    private lateinit var _binding: FragmentChoXacNhanBinding
    private val binding get() = _binding

    private val ordersService = OrdersService()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    private lateinit var viewModel: PurchaseHistoryViewModel
    private lateinit var adapter: RvPurchaseHistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoXacNhanBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[PurchaseHistoryViewModel::class.java]


        initView()
        getFirstPage()
        handleEvent()
        return binding.root
    }

    private fun initView(){
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrders.layoutManager = linearLayoutManager

        adapter = RvPurchaseHistoryAdapter(object : ClickObjectInterface<Order> {
            override fun onClickListener(t: Order) {
                val intent = Intent(context, PurchaseHistoryDetailsActivity::class.java)
                intent.putExtra("key_order",t)
                startActivity(intent)
            }
        })
        binding.rvOrders.adapter = adapter

        binding.rvOrders.addOnScrollListener(object :
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
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFirstPage() {
        binding.swipRefresh.isRefreshing = true
        isLoading = true
        Handler().postDelayed({
            ordersService.getFirstChoXacNhan{ list ->
                list?.value?.let {
                    adapter.setData(it)
                }
                if (list?.value?.isEmpty() == true) {
                    binding.lnChuaCoDonHang.visibility = View.VISIBLE
                    isLastPage = true
                }
//                else if (list?.value?.size!! <=0 ) {
//                        isLastPage = true
//                        Toast.makeText(context, "Hết dữ liệu", Toast.LENGTH_SHORT).show()
//                    }
            }
            isLoading = false
            binding.swipRefresh.isRefreshing = false
        }, 1500)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNext() {
        binding.swipRefresh.isRefreshing = true
        Handler().postDelayed({
            ordersService.loadNextListChoXacNhan { list ->
                list?.value?.let {
                    adapter.addData(it)
                }

                if (list == null) {
                    isLastPage = true
                }
            }

            isLoading = false
            binding.swipRefresh.isRefreshing = false
        }, 1500)
    }


    private fun handleEvent(){
        binding.swipRefresh.setOnRefreshListener {
            getFirstPage()
        }
    }


}


