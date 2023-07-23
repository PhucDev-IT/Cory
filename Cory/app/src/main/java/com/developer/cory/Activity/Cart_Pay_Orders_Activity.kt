package com.developer.cory.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.developer.cory.databinding.ActivityCartPayOrdersBinding

class Cart_Pay_Orders_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCartPayOrdersBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartPayOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}