package com.developer.cory.FragmentLayout


import android.annotation.SuppressLint
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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.PurchaseHistoryDetailsActivity
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Adapter.RvTestAdapter
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Order
import com.developer.cory.Model.PaginationScrollListener
import com.developer.cory.Model.Temp
import com.developer.cory.Service.OrdersService
import com.developer.cory.ViewModel.PurchaseHistoryViewModel
import com.developer.cory.databinding.FragmentChoXacNhanBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.snapshots


class ChoXacNhanFragment : Fragment() {
    private lateinit var _binding: FragmentChoXacNhanBinding
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
        _binding = FragmentChoXacNhanBinding.inflate(inflater, container, false)


        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrders.layoutManager = linearLayoutManager

        adapter = RvPurchaseHistoryAdapter( object : RvInterface {
            override fun onClickListener(pos: Int) {
                val intent = Intent(context,PurchaseHistoryDetailsActivity::class.java)
                intent.putExtra("key_order",listOrder[pos])
                startActivity(intent)
            }
        })
        binding.rvOrders.adapter = adapter

        binding.rvOrders.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItem() {
                Toast.makeText(context,"???",Toast.LENGTH_SHORT).show()
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
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        binding.progressBarLoading.visibility = View.VISIBLE
        isLoading = true
        Handler().postDelayed({
            ordersService.choXacNhan { list->
                listOrder.addAll(list)
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                if(list.isEmpty()){
                    isLastPage = true
                }
            }
            isLoading = false
            binding.progressBarLoading.visibility = View.GONE
        },1500)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNext(){
        binding.progressBarLoading.visibility = View.VISIBLE
        isLoading = true
        Handler().postDelayed({
            ordersService.choXacNhan { list->
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                if(list.isEmpty()){
                    isLastPage = true
                }
            }
            Toast.makeText(context,"Súc sinh",Toast.LENGTH_SHORT).show()
            isLoading = false
            binding.progressBarLoading.visibility = View.GONE
        },1500)
    }

}


