package com.developer.cory.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOptionAddressAdapter
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.databinding.FragmentOptionAddressBinding


class OptionAddressFragment : Fragment() {
  private lateinit var _binding:FragmentOptionAddressBinding
    private val binding get() = _binding
    private val addressService = AddressService()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionAddressBinding.inflate(inflater,container,false)
        initView()
        return  binding.root
    }

    private fun initView(){
        Temp.user?.id?.let {
            addressService.selectByIDUser(it){ list->
                val adapter = RvOptionAddressAdapter(list, object : RvInterface{
                    override fun onClickListener(pos: Int) {
                        Toast.makeText(context,"Bạn vừa chọn: ${list[pos].tinhThanhPho}",Toast.LENGTH_SHORT).show()
                    }
                })

                binding.rvAddress.adapter = adapter
                binding.rvAddress.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            }
        }
    }
}