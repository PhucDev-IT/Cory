package com.example.cory_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cory_admin.Fragment.ProductFragment
import com.example.cory_admin.Fragment.StaffFragment
import com.example.cory_admin.Fragment.ViewMoreFragment
import com.example.cory_admin.Fragment.VoucherFragment
import com.example.cory_admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mnuHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ProductFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuVoucher ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, VoucherFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }


                R.id.mnuUser ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, StaffFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menuOther ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ViewMoreFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {return@setOnNavigationItemSelectedListener false}
            }
            }

        // Hiển thị Fragment mặc định khi chạy app
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, ProductFragment())
                .commit()
        }
    }
}