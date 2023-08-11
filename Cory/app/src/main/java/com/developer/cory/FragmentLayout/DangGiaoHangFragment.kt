package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.PurchaseHistoryDetailsActivity
import com.developer.cory.Adapter.RvPurchaseHistoryAdapter
import com.developer.cory.Interface.RvInterface
import com.developer.cory.R
import com.developer.cory.Service.OrdersService
import com.developer.cory.databinding.FragmentChoXacNhanBinding
import com.developer.cory.databinding.FragmentDangGiaoHangBinding

class DangGiaoHangFragment : Fragment() {
    private lateinit var _binding: FragmentDangGiaoHangBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDangGiaoHangBinding.inflate(inflater,container,false)

        getValues()
        return binding.root
    }



    private fun getValues() {
//        OrdersService().dangGiaoHang { list->
//            val adapter = RvPurchaseHistoryAdapter(list,object : RvInterface {
//                override fun onClickListener(pos: Int) {
//                    val intent = Intent(context, PurchaseHistoryDetailsActivity::class.java)
//                    intent.putExtra("key_order",list[pos])
//                    startActivity(intent)
//                }
//            })
//            binding.rvDangGiaoHang.adapter = adapter
//            binding.rvDangGiaoHang.layoutManager = LinearLayoutManager(context,
//                LinearLayoutManager.VERTICAL,false)
//
//        }

    }
}