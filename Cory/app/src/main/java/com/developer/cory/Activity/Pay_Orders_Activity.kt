package com.developer.cory.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOrdersAdapter
import com.developer.cory.Model.Order
import com.developer.cory.Model.Product
import com.developer.cory.databinding.ActivityPayOrdersBinding

class Pay_Orders_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOrdersBinding
    private var listProducts = mutableListOf<Order>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listProducts.add(Order(Product("Mì cay cấp 7", 46456456.0),32))
        listProducts.add(Order(Product("Coffe đá", 567567.2),2))

        val adapter = RvOrdersAdapter(listProducts)
        binding.rvOrders.adapter = adapter
        binding.rvOrders.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
    }
}