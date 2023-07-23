package com.developer.cory.Adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Model.Address
import com.developer.cory.R

class RvItemAddressAdapter(private val list:List<Address>):RecyclerView.Adapter<RvItemAddressAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item_address,parent,false)
        return viewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            val tvName = findViewById<TextView>(R.id.tvFullName)
            val tvPhone = findViewById<TextView>(R.id.tvNumberPhone)
            val tvAddress = findViewById<TextView>(R.id.tvAddress)
            val tvAddressDetails = findViewById<TextView>(R.id.tvAddressDetails)

            tvName.text = list[position].fullName
            tvPhone.text = list[position].numberPhone
            tvAddress.text = "${list[position].phuongXa} , ${list[position].quanHuyen}, ${list[position].tinhThanhPho}"
            tvAddressDetails.text = list[position].addressDetails
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}