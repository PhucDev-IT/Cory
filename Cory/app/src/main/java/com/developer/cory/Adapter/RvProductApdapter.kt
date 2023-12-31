package com.developer.cory.Activity
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Interface.ClickObjectInterface
import com.developer.cory.Model.Product
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.R
import java.text.NumberFormat
import java.util.Locale


class RvProductApdapter(private var list:List<Product>, private val onClick: ClickObjectInterface<Product>):
    RecyclerView.Adapter<RvProductApdapter.viewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>){
        this.list = list
        notifyDataSetChanged()
    }

    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product,parent,false)
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

           Glide.with(context).load(list[position].img_url).into(imgProduct)
           tvPrice.text = FormatCurrency.numberFormat.format(list[position].price)
           tvNameProduct.text = list[position].name


           holder.itemView.setOnClickListener{
               onClick.onClickListener(list[position])
           }
       }
    }
}