package com.example.cory_admin.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cory_admin.Model.FormatCurrency
import com.example.cory_admin.R

class RvItemAddProductAdapter():RecyclerView.Adapter<RvItemAddProductAdapter.viewHolder>() {

    private var list:MutableMap<String,Double> = hashMapOf()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(lst:MutableMap<String,Double>){
        if(lst.isNotEmpty()){
            list.putAll(lst)
            notifyItemInserted(list.size - lst.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reset(){
        list.clear()
        notifyDataSetChanged()
    }

    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         val tvTitle:TextView
         val tvPrice:TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvPrice = itemView.findViewById(R.id.tvPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item_add_product,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val title = list.keys.elementAt(position)
        val price = list.values.elementAt(position)

        holder.tvTitle.text = title
        holder.tvPrice.text = FormatCurrency.numberFormat.format(price)


    }

    override fun getItemCount(): Int {
        if(list.isEmpty()){
            return 0
        }
        return list.size
    }
}