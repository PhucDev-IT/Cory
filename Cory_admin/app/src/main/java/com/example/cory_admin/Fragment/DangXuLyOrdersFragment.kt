package com.example.cory_admin.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cory_admin.Activity.OrderDetailsActivity
import com.example.cory_admin.Adapter.OrderManagerAdapter
import com.example.cory_admin.Interface.RvInterface
import com.example.cory_admin.Model.Order
import com.example.cory_admin.Model.PaginationScrollListener
import com.example.cory_admin.Service.OrdersService
import com.example.cory_admin.ViewModel.OrdersManagerViewModel
import com.example.cory_admin.databinding.FragmentChoXacNhanBinding
import com.example.cory_admin.databinding.FragmentDangXuLyOrdersBinding


class DangXuLyOrdersFragment : Fragment() {
    private lateinit var _binding:FragmentDangXuLyOrdersBinding
    private val binding get() = _binding

    private val ordersService = OrdersService()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    private lateinit var adapter: OrderManagerAdapter
    private var listOrder = mutableListOf<Order>()

    private val viewModel:OrdersManagerViewModel = OrdersManagerViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDangXuLyOrdersBinding.inflate(inflater, container, false)


        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrders.layoutManager = linearLayoutManager

        adapter = OrderManagerAdapter( object : RvInterface {
            override fun onClickListener(pos: Int) {
                val intent = Intent(context, OrderDetailsActivity::class.java)
                intent.putExtra("key_order",listOrder[pos])
                startActivity(intent)
            }
        })
        binding.rvOrders.adapter = adapter

        binding.rvOrders.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItem() {
                isLoading = true
                loadData()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })

        loadData()
        observeViewModel()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        binding.progressBarLoading.visibility = View.VISIBLE

        Handler().postDelayed({

            viewModel.getListOrderDangChoXuLy()
            isLoading = false
            binding.progressBarLoading.visibility = View.GONE
        },1500)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel(){
        viewModel.listOrdersDangChoXuLy.observe(viewLifecycleOwner) { list->
            if(list==null){
                Toast.makeText(context,"Aỏ chó",Toast.LENGTH_SHORT).show()
                binding.lnHaveNotOrders.visibility = View.VISIBLE
                binding.rvOrders.visibility = View.GONE
                isLastPage = true
            }
            if(list.isNotEmpty()){
                listOrder.addAll(list)
                adapter.setData(list)
                adapter.notifyDataSetChanged()

            }
        }
    }
}