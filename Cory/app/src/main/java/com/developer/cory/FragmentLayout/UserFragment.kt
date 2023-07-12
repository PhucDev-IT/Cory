package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.cory.Activity.LoginActivity
import com.developer.cory.Activity.RegisterActivity

import com.developer.cory.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private  var _binding: FragmentUserBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater,container,false)
        onClick()
        return binding.root
    }



    fun onClick(){
        binding.btnLogin.setOnClickListener(){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }

        binding.btnResgister.setOnClickListener(){
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}