package com.developer.cory.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.R


class RvProductInSearchApdapter(private var list:List<Product>, private val onClick: RvInterface):RecyclerView.Adapter<RvProductInSearchApdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    fun setData(list: List<Product>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product_search,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.itemView.apply {
           val imgProduct = findViewById<ImageView>(R.id.imgProduct)
           val tvNameProduct = findViewById<TextView>(R.id.tvNameProduct)
           val tvPrice = findViewById<TextView>(R.id.tvPrice)
           val tvContent = findViewById<TextView>(R.id.tvContent)

           tvPrice.text = list[position].price.toString()+"đ"
           tvNameProduct.text = list[position].name
           tvContent.text = list[position].description

          //  imgProduct.setImageResource(list[position].url)
           holder.itemView.setOnClickListener{
               onClick.onClickListener(position)
           }
       }
    }
}