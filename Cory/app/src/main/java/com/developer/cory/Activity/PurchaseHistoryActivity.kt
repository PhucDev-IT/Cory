package com.developer.cory.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.developer.cory.Adapter.ViewPagerPurchasedAdapter
import com.developer.cory.Model.EnumOrder
import com.developer.cory.databinding.ActivityPurchaseHistoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class PurchaseHistoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPurchaseHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPagerPurchasedAdapter:ViewPagerPurchasedAdapter = ViewPagerPurchasedAdapter(this)
        binding.viewPager2.adapter = viewPagerPurchasedAdapter

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
            EnumOrder.CHOXACNHAN.name -> binding.viewPager2.currentItem = 0
            EnumOrder.DANGGIAOHANG.name -> binding.viewPager2.currentItem = 1
            EnumOrder.GIAOHANGTHANHCONG.name -> binding.viewPager2.currentItem = 2
            else -> binding.viewPager2.currentItem = 0
        }
    }
}