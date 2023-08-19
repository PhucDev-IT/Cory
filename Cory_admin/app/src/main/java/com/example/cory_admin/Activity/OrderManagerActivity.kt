package com.example.cory_admin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.cory_admin.Adapter.ViewPagerAdapter
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.R
import com.example.cory_admin.databinding.ActivityOrderManagerBinding
import com.google.android.material.tabs.TabLayout

class OrderManagerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2.adapter = viewPagerAdapter

        binding.tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
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

        initView()
    }

    private fun initView(){
        val tab = intent.getStringExtra("key_tab")

        when(tab){
            EnumOrder.CHOXACNHAN.name -> binding.viewPager2.currentItem = 0
            EnumOrder.DANGGIAOHANG.name -> binding.viewPager2.currentItem = 1
            else -> binding.viewPager2.currentItem = 0
        }
    }
}