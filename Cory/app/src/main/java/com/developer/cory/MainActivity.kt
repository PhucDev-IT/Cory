package com.developer.cory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.developer.cory.FragmentLayout.CartFragment
import com.developer.cory.FragmentLayout.HomeFragment
import com.developer.cory.FragmentLayout.NotificationFragment
import com.developer.cory.FragmentLayout.UserFragment
import com.developer.cory.Model.User
import com.developer.cory.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataUser()

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

    //Lấy thông tin người dùng
    private fun getDataUser()
    {
        val numberPhone = intent.getStringExtra("phone")

        val db = Firebase.firestore
        db.collection("Users").whereEqualTo("numberPhone",numberPhone)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    user = document.toObject(User::class.java)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Có lỗi xảy ra, Hãy liên hệ với nhà phát triển",Toast.LENGTH_LONG).show()
                finish()
            }
    }

}