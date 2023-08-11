package com.developer.cory.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.developer.cory.FragmentLayout.DangGiaoHangFragment
import com.developer.cory.FragmentLayout.LichSuMuaHangFragment
import com.developer.cory.FragmentLayout.ChoXacNhanFragment

class ViewPagerAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)


    override fun getItemCount(): Int {
        return 100
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ChoXacNhanFragment()
            1-> DangGiaoHangFragment()
            2-> LichSuMuaHangFragment()
            else -> LichSuMuaHangFragment()
        }
    }
}