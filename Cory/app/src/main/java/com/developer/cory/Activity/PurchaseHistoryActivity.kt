package com.developer.cory.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import com.developer.cory.Adapter.ViewPagerAdapter
import com.developer.cory.R
import com.developer.cory.databinding.ActivityPurchaseHistoryBinding
import com.developer.cory.databinding.FragmentAddressDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator

class PurchaseHistoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPurchaseHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPagerAdapter:ViewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager2) { tab, position ->
            when(position){
                0 -> tab.text = "Chờ xác nhận"
                1-> tab.text = "Đang giao hàng"
                2->tab.text = "Đã mua"
            }
        }
    }
}