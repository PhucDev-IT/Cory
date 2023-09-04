package com.developer.cory.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.developer.cory.FragmentLayout.DangGiaoHangFragment
import com.developer.cory.FragmentLayout.LichSuMuaHangFragment
import com.developer.cory.FragmentLayout.ChoXacNhanFragment
import com.developer.cory.FragmentLayout.SignUpWithEmailFragment
import com.developer.cory.FragmentLayout.SignUpWithPhoneFragment

class ViewPagerRegisterAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)


    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SignUpWithPhoneFragment()
            1-> SignUpWithEmailFragment()
            else -> SignUpWithPhoneFragment()
        }
    }
}