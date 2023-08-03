package com.developer.cory.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvChoXacNhanAdapter
import com.developer.cory.Model.Order
import com.developer.cory.R
import com.developer.cory.Service.OrdersService
import com.developer.cory.databinding.FragmentChoXacNhanBinding

class ChoXacNhanFragment : Fragment() {
    private lateinit var _binding:FragmentChoXacNhanBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoXacNhanBinding.inflate(inflater,container,false)

        getValues()
        return binding.root
    }



    private fun getValues() {
        OrdersService().donHangChoXacNhan { list->
            val adapter = RvChoXacNhanAdapter(list)
            binding.rvOrders.adapter = adapter
            binding.rvOrders.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        }

    }
}