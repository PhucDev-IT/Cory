package com.developer.cory.FragmentLayout


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val sharedViewModel: PurchaseHistoryViewModel by activityViewModels()

    private val ordersService = OrdersService()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var sizeOld: Int = 0
    private lateinit var adapter: RvPurchaseHistoryAdapter
    private var lastKey: String? = ""
    private var list: MutableList<Order> = ArrayList()

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
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        binding.progressBarLoading.visibility = View.VISIBLE
        ordersService.getValues{list ->
            adapter.setData(list)
            adapter.notifyDataSetChanged()
            binding.progressBarLoading.visibility = View.GONE
            isLoading = false
            if(list.isEmpty()){
                isLastPage = true
            }
        }
    }
}


