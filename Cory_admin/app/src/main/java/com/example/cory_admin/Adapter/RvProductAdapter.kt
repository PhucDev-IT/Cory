package com.developer.cory.Activity
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.cory_admin.Interface.RvInterface
import com.example.cory_admin.Model.FormatCurrency
import com.example.cory_admin.Model.Product
import com.example.cory_admin.R


class RvProductAdapter(private var list:List<Product>, private val onClick: RvInterface):RecyclerView.Adapter<RvProductAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_products,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.itemView.apply {
           val imgProduct = findViewById<ImageView>(R.id.imgProduct)
           val tvNameProduct = findViewById<TextView>(R.id.tvNameProduct)
           val tvPrice = findViewById<TextView>(R.id.tvPrice)
           val tvContent = findViewById<TextView>(R.id.tvContent)

           tvPrice.text = FormatCurrency.numberFormat.format(list[position].price)
           tvNameProduct.text = list[position].name
           tvContent.text = list[position].description

           Glide.with(context).load(list[position].img_url).into(imgProduct)
           holder.itemView.setOnClickListener{
               onClick.onClickListener(position)
           }
       }
    }
}