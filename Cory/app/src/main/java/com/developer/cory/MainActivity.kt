package com.developer.cory

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.developer.cory.Activity.Cart_Pay_Orders_Activity
import com.developer.cory.FragmentLayout.CartFragment
import com.developer.cory.FragmentLayout.HomeFragment
import com.developer.cory.FragmentLayout.NotificationFragment
import com.developer.cory.FragmentLayout.UserFragment
import com.developer.cory.Model.Temp
import com.developer.cory.Model.User
import com.developer.cory.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var user: User?=null

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
                        .addToBackStack(null)
                        .commit()
                    
                    return@setOnNavigationItemSelectedListener true
                }


                R.id.mnuNotification -> {
                    if(user==null){
                        Toast.makeText(this,"Vui lòng đăng nhập",Toast.LENGTH_SHORT).show()
                        return@setOnNavigationItemSelectedListener false
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, NotificationFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuPerson -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UserFragment())
                        .addToBackStack(null)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.mnuDatHang -> {
                    val intent = Intent(this,Cart_Pay_Orders_Activity::class.java)
                    startActivity(intent)
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
        val numberPhone = intent.getStringExtra("key_phone")

        val db = Firebase.firestore
        db.collection("Users").whereEqualTo("numberPhone",numberPhone)
            .get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty) {
                    val document = documents.documents[0] // lấy tài liệu đầu tiên trong danh sách
                    user = document.toObject(User::class.java)!!
                    user?.id = document.id
                    Temp.user = user
                }
            }
            .addOnFailureListener { er->
                Toast.makeText(this,"Có lỗi xảy ra, Hãy liên hệ với nhà phát triển",Toast.LENGTH_LONG).show()
                Log.e(TAG,"Lỗi: ",er)
                finish()
            }
    }

}