package com.example.cory_admin.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cory_admin.Activity.AddVoucherActivity
import com.example.cory_admin.Adapter.RvVoucherAdapter
import com.example.cory_admin.Model.PaginationScrollListener
import com.example.cory_admin.Service.VoucherService
import com.example.cory_admin.databinding.FragmentVoucherBinding


class VoucherFragment : Fragment(),OnClickListener {

    private lateinit var _binding:FragmentVoucherBinding
    private val binding get() = _binding
    private val voucherService = VoucherService()
    private lateinit var adapter:RvVoucherAdapter

    private var isLoading = false
    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoucherBinding.inflate(inflater,container,false)
        registerClickEvent()


        adapter = RvVoucherAdapter()
        binding.rvVoucher.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvVoucher.layoutManager = linearLayoutManager

        binding.rvVoucher.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItem() {
               isLoading = true
               // binding.progressbar.visibility = View.VISIBLE
                getVoucher()
            }

            override fun isLoading(): Boolean {
               return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })

        intiView()
        return binding.root
    }

    private fun intiView(){
        getVoucher()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getVoucher(){
        binding.progressbar.visibility = View.VISIBLE
        Handler().postDelayed({
            voucherService.selectVoucher { list ->
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                isLoading = false
                binding.progressbar.visibility = View.GONE
                if(list.isEmpty()){
                    isLastPage = true
                    Toast.makeText(context,"Hết rồi",Toast.LENGTH_SHORT).show()
                }
            }
        },1000)
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