package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Model.Order
import com.developer.cory.R


class RvOrdersAdapter(private val list:List<Order>): RecyclerView.Adapter<RvOrdersAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_orderdetails,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
           val nameProd = findViewById<TextView>(R.id.tvNameProduct)
            val price = findViewById<TextView>(R.id.tvPrice)
            val quantity = findViewById<TextView>(R.id.tvQuantity)
            val img = findViewById<ImageView>(R.id.imgProduct)

            nameProd.text = list[position].product?.name
            price.text = list[position].product?.price.toString()
            quantity.text = list[position].quantity.toString()
        }
    }
}