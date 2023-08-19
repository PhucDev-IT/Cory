package com.example.cory_admin.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cory_admin.Fragment.ChoXacNhanFragment
import com.example.cory_admin.Fragment.DangXuLyOrdersFragment

class ViewPagerAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)


    override fun getItemCount(): Int {
        return 100
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ChoXacNhanFragment()
            1 -> DangXuLyOrdersFragment()
            else -> ChoXacNhanFragment()
        }
    }
}