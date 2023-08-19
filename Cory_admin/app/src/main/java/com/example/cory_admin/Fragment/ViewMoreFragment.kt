package com.example.cory_admin.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.example.cory_admin.Activity.OrderManagerActivity
import com.example.cory_admin.Adapter.OrderManagerAdapter
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.R
import com.example.cory_admin.databinding.FragmentChoXacNhanBinding
import com.example.cory_admin.databinding.FragmentViewmoreBinding

class ViewMoreFragment : Fragment(), OnClickListener {
    private lateinit var _binding: FragmentViewmoreBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewmoreBinding.inflate(inflater,container,false)
        handlerEvent()


        return binding.root
    }

    private fun handlerEvent(){
        binding.lnChoXacNhan.setOnClickListener(this)
        binding.lnDangXuLy.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(context,OrderManagerActivity::class.java)
        when(p0?.id){
            R.id.lnChoXacNhan ->{
                intent.putExtra("key_tab",EnumOrder.CHOXACNHAN.name)
            }

            R.id.lnDangXuLy ->{
                intent.putExtra("key_tab",EnumOrder.DANGGIAOHANG.name)
            }

            else ->{
                intent.putExtra("key_tab",EnumOrder.CHOXACNHAN.name)
            }
        }
        startActivity(intent)
    }
}