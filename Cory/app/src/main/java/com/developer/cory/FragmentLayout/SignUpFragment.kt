package com.developer.cory.FragmentLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.developer.cory.Adapter.ViewPagerPurchasedAdapter
import com.developer.cory.Adapter.ViewPagerRegisterAdapter
import com.developer.cory.R
import com.developer.cory.ViewModel.RegisterViewModel
import com.developer.cory.databinding.FragmentSignUpBinding
import com.google.android.material.tabs.TabLayout


class SignUpFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpBinding
    private val binding get() = _binding
    private lateinit var navController: NavController
    private val viewModel : RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)

        var viewPagerPurchasedAdapter: ViewPagerRegisterAdapter = ViewPagerRegisterAdapter(requireActivity())
        binding.viewPager2.adapter = viewPagerPurchasedAdapter

        binding.tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.reset()
                if (tab != null) {
                    binding.viewPager2.currentItem = tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)?.select()
            }
        })


        viewModel.isAllowSignUp.observe(viewLifecycleOwner) { b ->
            if (b) {
                binding.btnRegister.visibility = View.VISIBLE
                if(viewModel.signUpWith.equals("email")){
                    navController.navigate(R.id.action_signUpFragment_to_verifyEmailFragment)

                }
            }
        }

        handleClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun handleClick(){
        binding.btnRegister.setOnClickListener {
            if(viewModel.signUpWith.equals("phone")) {
                navController.navigate(R.id.action_signUpFragment_to_verifyOtpFragment)
            }
        }
    }

}