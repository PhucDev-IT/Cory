package com.developer.cory.FragmentLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developer.cory.R
import com.developer.cory.databinding.FragmentMainSettingsBinding

class MainSettingsFragment : Fragment(),View.OnClickListener {

    private lateinit var _binding:FragmentMainSettingsBinding
    private val binding get() = _binding
    private lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainSettingsBinding.inflate(inflater,container,false)

        binding.lnAddAddress.setOnClickListener(this)
        binding.lnSecurityAccount.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.lnAddAddress -> navController.navigate(R.id.action_mainSettingsFragment_to_myAddressFragment)

            R.id.lnSecurityAccount -> navController.navigate(R.id.action_mainSettingsFragment_to_securityAccountFragment)
        }
    }


}