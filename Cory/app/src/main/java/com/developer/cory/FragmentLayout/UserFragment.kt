package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.cory.Activity.LoginActivity
import com.developer.cory.Activity.PurchaseHistoryActivity
import com.developer.cory.Activity.RegisterActivity
import com.developer.cory.Activity.SettingsActivity
import com.developer.cory.Model.Temp
import com.developer.cory.R

import com.developer.cory.databinding.FragmentUserBinding
import com.developer.cory.modules.ChoXacNhanFragment

class UserFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        onClick()
        init()
        return binding.root
    }

    private fun init() {
        if (Temp.user != null) {
            binding.lnAuth.visibility = View.GONE
            binding.lnProfileUser.visibility = View.VISIBLE

            if (Temp.user!!.name != null) {
                binding.tvFullName.visibility = View.VISIBLE
                binding.tvFullName.text = Temp.user!!.name
            }
        }
    }


    fun onClick() {

        binding.lnThietLapTaiKhoan.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener() {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }

        binding.btnResgister.setOnClickListener() {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.imgbtnSetting.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.lnKhoVoucher.setOnClickListener{

        }

        binding.lnLichSuMuaHang.setOnClickListener(this)

        binding.lnChoXacNhan.setOnClickListener(this)
        binding.lnDangGiao.setOnClickListener(this)
    }

    @SuppressLint("CommitTransaction")
    fun convertScreen(view: View) {

        when (view) {
            binding.lnLichSuMuaHang -> {
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab","Đơn mua")
                startActivity(intent)
            }

            binding.lnChoXacNhan ->{
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab","Chờ xác nhận")
                startActivity(intent)
            }

            binding.lnDangGiao ->{
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab","Đang giao")
                startActivity(intent)
            }
        }

    }

    override fun onClick(view: View?) {
        if (view != null) {
            convertScreen(view)
        }
    }
}