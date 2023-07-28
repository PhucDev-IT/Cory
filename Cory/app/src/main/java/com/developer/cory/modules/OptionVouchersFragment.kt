package com.developer.cory.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOptionVouchersAdapter
import com.developer.cory.Adapter.RvVoucherAdapter
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.Voucher
import com.developer.cory.R
import com.developer.cory.Service.VoucherService
import com.developer.cory.ViewModel.PayOrderViewModel
import com.developer.cory.databinding.FragmentOptionVouchersBinding

class OptionVouchersFragment : Fragment() {
  private lateinit var _binding:FragmentOptionVouchersBinding
    private val binding get() = _binding
    private val voucherService = VoucherService()
    private lateinit var voucherFree:Voucher
    private val sharedViewModel: PayOrderViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionVouchersBinding.inflate(inflater,container,false)
        initView()
        event()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun initView()
    {
        voucherService.selectVoucher { list->
            val adapter = RvOptionVouchersAdapter(list,object : RvInterface{
                override fun onClickListener(pos: Int) {
                    voucherFree = list[pos]
                }
            })
            binding.rvVoucherFreeShip.adapter = adapter
            binding.rvVoucherFreeShip.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
        }
    }

    fun event()
    {
        binding.floatingActionButton.setOnClickListener {
            sharedViewModel.setVoucher(voucherFree)
            navController.popBackStack()
        }
    }

}