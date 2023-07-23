package com.developer.cory.FragmentLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.cory.R
import com.developer.cory.Service.VoucherService
import com.developer.cory.databinding.FragmentMyVoucherBinding


class MyVoucherFragment : Fragment() {
  private lateinit var _binding:FragmentMyVoucherBinding
    private val binding get() =  _binding
    private val voucherService = VoucherService()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyVoucherBinding.inflate(inflater,container,false)

        return binding.root
    }

    private fun initView(){
        voucherService.selectVoucher{list->

        }
    }
}