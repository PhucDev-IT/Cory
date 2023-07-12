package com.developer.cory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.developer.cory.FragmentLayout.CartFragment
import com.developer.cory.FragmentLayout.HomeFragment
import com.developer.cory.FragmentLayout.NotificationFragment
import com.developer.cory.FragmentLayout.UserFragment
import com.developer.cory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mnuHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, HomeFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuNotification -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, NotificationFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuPerson -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UserFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuDatHang -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, CartFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> { return@setOnNavigationItemSelectedListener false}
            }
        }

        // Hiển thị Fragment mặc định khi chạy app
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HomeFragment())
                .commit()
        }

    }

}