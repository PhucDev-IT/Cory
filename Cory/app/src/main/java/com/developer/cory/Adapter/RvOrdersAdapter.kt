package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Order
import com.developer.cory.R


class RvOrdersAdapter(private val list:List<CartModel>): RecyclerView.Adapter<RvOrdersAdapter.viewHolder>() {

    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         var nameProduct : TextView
        var price : TextView
        var quantity : TextView
         var imgProduct :ImageView
         var classify : TextView
         var sideDishes : TextView

        init {
            nameProduct = itemView.findViewById(R.id.tvNameProduct)
            quantity = itemView.findViewById(R.id.tvQuantity)
            imgProduct = itemView.findViewById(R.id.imgProduct)
            classify = itemView.findViewById(R.id.tvClassify)
            sideDishes = itemView.findViewById(R.id.tvSideDishes)
            price = itemView.findViewById(R.id.tvPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_orderdetails,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(list[position].product?.img_url).into(holder.imgProduct)
            holder.nameProduct.text = list[position].product?.name
            holder.price.text = FormatCurrency.numberFormat.format(list[position].totalMoney)
            holder.quantity.text = "x${list[position].quantity}"

            if(list[position].classify?.isEmpty() == true){
                holder.classify.visibility = View.GONE
            }else{
                holder.classify.text = list[position].classify
            }

            if(list[position].sideDishes?.isEmpty() == true){
                holder.sideDishes.visibility = View.GONE
            }else{
                var dishes:String = "Thêm: "
                for(values in list[position].sideDishes!!){
                    dishes+= values
                }

                holder.sideDishes.text = dishes
            }
        }
    }
}