package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.cory.Activity.LoginActivity
import com.developer.cory.R
import com.developer.cory.databinding.FragmentRegisterCompletedBinding
import com.developer.cory.databinding.FragmentVerifyEmailBinding

class VerifyEmailFragment : Fragment() {
  private lateinit var _binding:FragmentVerifyEmailBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        handleEvent()
        return binding.root
    }

    private fun handleEvent(){
        binding.btnConvertLoginForm.setOnClickListener{
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}