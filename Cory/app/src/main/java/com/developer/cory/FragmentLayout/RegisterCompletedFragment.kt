package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.developer.cory.Activity.LoginActivity
import com.developer.cory.databinding.FragmentRegisterCompletedBinding



class RegisterCompletedFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterCompletedBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCompletedBinding.inflate(inflater, container, false)
        handleEvent()
        return binding.root
    }

    private fun handleEvent(){
        binding.btnConvertLoginForm.setOnClickListener{
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}