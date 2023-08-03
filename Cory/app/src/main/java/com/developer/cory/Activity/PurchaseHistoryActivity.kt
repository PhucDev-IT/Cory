package com.developer.cory.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.developer.cory.Adapter.ViewPagerAdapter
import com.developer.cory.R
import com.developer.cory.databinding.ActivityPurchaseHistoryBinding
import com.developer.cory.databinding.FragmentAddressDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class PurchaseHistoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPurchaseHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPagerAdapter:ViewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2.adapter = viewPagerAdapter

        binding.tabLayout.addOnTabSelectedListener(object  : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager2.currentItem = tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager2.registerOnPageChangeCallback(object :OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)?.select()
            }
        })

        initView()
    }

    private fun initView(){
        val tab = intent.getStringExtra("key_tab")

        when(tab){
            "Chờ xác nhận" -> binding.viewPager2.currentItem = 0
            "Đang giao" -> binding.viewPager2.currentItem = 1
            "Đơn mua" -> binding.viewPager2.currentItem = 2
            else -> binding.viewPager2.currentItem = 0
        }
    }
}