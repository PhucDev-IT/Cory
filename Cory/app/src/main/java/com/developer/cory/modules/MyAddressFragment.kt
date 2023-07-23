package com.developer.cory.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvItemAddressAdapter
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.databinding.FragmentMyAddressBinding

class MyAddressFragment : Fragment() {

    private lateinit var _binding : FragmentMyAddressBinding
    private val binding get() = _binding
    private val addressService = AddressService()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyAddressBinding.inflate(inflater,container,false)

        initView()

        binding.lnAddNewAddress.setOnClickListener {
           navController.navigate(R.id.action_myAddressFragment_to_addressDetailsFragment)

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun initView(){
        Temp.user?.id?.let {
            addressService.selectByIDUser(it){ list->
                val adapter = RvItemAddressAdapter(list)
                binding.rvAddress.adapter = adapter
                binding.rvAddress.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

            }
        }
    }
}