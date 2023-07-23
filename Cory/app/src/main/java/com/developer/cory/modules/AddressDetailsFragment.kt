package com.developer.cory.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.developer.cory.Model.Address
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.databinding.FragmentAddressDetailsBinding


class AddressDetailsFragment : Fragment() {

    private lateinit var _binding: FragmentAddressDetailsBinding
    private val binding get() = _binding
    private val addressService = AddressService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressDetailsBinding.inflate(inflater,container,false)

        binding.btnHoanThanh.setOnClickListener {
            if(binding.edtFullName.text.toString().trim().isEmpty() ||
                    binding.edtNumberPhone.text.toString().trim().isEmpty() ||
                binding.edtTinhThanhPho.text.toString().trim().isEmpty() ||
                binding.edtQuanHuyen.text.toString().trim().isEmpty() ||
                binding.edtPhuongXa.text.toString().trim().isEmpty() ||
                    binding.edtDetailsAddress.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show()
            }else{

                val address:Address = Address(binding.edtFullName.text.toString().trim(),
                    binding.edtNumberPhone.text.toString().trim(),
                    binding.edtTinhThanhPho.text.toString().trim(),
                    binding.edtQuanHuyen.text.toString().trim(),
                    binding.edtPhuongXa.text.toString().trim(),
                    binding.edtDetailsAddress.text.toString().trim()
                )

                Temp.user?.id?.let { it1 ->
                    addressService.insertAddress(address, it1){ b: Boolean ->
                        if(b){
                            Toast.makeText(context,"Thêm thành công",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,"Có lỗi xảy ra, thử lại sau.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }



            }
        }

        return binding.root
    }




}