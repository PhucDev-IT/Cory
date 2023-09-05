package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developer.cory.Activity.LoginActivity
import com.developer.cory.databinding.FragmentDefaultNotLoginBinding


class DefaultNotLoginFragment : Fragment() {
    private lateinit var _binding:FragmentDefaultNotLoginBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDefaultNotLoginBinding.inflate(inflater,container,false)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }
}