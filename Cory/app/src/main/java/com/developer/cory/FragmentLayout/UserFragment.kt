package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.developer.cory.Activity.LoginActivity
import com.developer.cory.Activity.PurchaseHistoryActivity
import com.developer.cory.Activity.RegisterActivity
import com.developer.cory.Activity.SettingsActivity
import com.developer.cory.MainActivity
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.Temp
import com.developer.cory.R

import com.developer.cory.databinding.FragmentUserBinding

class UserFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog

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

    @SuppressLint("SetTextI18n")
    private fun init() {
        if (Temp.user != null) {
            binding.btnLogout.visibility = View.VISIBLE
            binding.lnAuth.visibility = View.GONE
            binding.lnProfileUser.visibility = View.VISIBLE

            if (Temp.user!!.name != null) {
                binding.tvFullName.visibility = View.VISIBLE
                binding.tvFullName.text = Temp.user!!.name
            }
        }

        Temp.account?.numberXu?.let { binding.tvNumberXu.text = "$it xu" }
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
        binding.btnLogout.setOnClickListener(this)
    }

    @SuppressLint("CommitTransaction")
    fun convertScreen(view: View) {
        if(Temp.user == null){
            Toast.makeText(context,"Đăng nhập để thực hiện tác vụ này.",Toast.LENGTH_SHORT).show()
            return
        }
        when (view) {
            binding.lnLichSuMuaHang -> {
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab",EnumOrder.GIAOHANGTHANHCONG.name)
                startActivity(intent)
            }

            binding.lnChoXacNhan ->{
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab",EnumOrder.CHOXACNHAN.name)
                startActivity(intent)
            }

            binding.lnDangGiao ->{
                val intent = Intent(context,PurchaseHistoryActivity::class.java)
                intent.putExtra("key_tab",EnumOrder.DANGGIAOHANG.name)
                startActivity(intent)
            }

            binding.btnLogout->{
                logout()
            }
        }

    }

    @SuppressLint("MissingInflatedId")
    fun logout(){
        val build = AlertDialog.Builder(context, R.style.CustomAlert)
        val viewDialog = layoutInflater.inflate(R.layout.dialog_logout, null)

        val btnOk = viewDialog.findViewById<Button>(R.id.btnOk)
        val btnNo = viewDialog.findViewById<Button>(R.id.btnNo)

        btnOk.setOnClickListener {
            val intent = Intent(context,MainActivity::class.java)
            Temp.reset()
            startActivity(intent)
            activity?.finish()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        build.setView(viewDialog)
        build.setCancelable(false)
        dialog = build.create()
        dialog.show()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            convertScreen(view)
        }
    }
}