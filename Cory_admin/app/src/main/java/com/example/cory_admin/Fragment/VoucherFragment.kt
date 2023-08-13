package com.example.cory_admin.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cory_admin.Activity.AddVoucherActivity
import com.example.cory_admin.databinding.FragmentVoucherBinding


class VoucherFragment : Fragment(),OnClickListener {

    private lateinit var _binding:FragmentVoucherBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoucherBinding.inflate(inflater,container,false)
        registerClickEvent()
        return binding.root
    }

    private fun registerClickEvent(){
        binding.fabAdd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.fabAdd -> {
                val intent = Intent(context,AddVoucherActivity::class.java)
                startActivity(intent)
            }
        }
    }
}