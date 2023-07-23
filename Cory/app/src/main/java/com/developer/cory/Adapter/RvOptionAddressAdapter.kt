package com.developer.cory.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Interface.RvInterface
import com.developer.cory.Model.Address
import com.developer.cory.R

class RvOptionAddressAdapter(private val list:List<Address>, private val onClick:RvInterface):RecyclerView.Adapter<RvOptionAddressAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var rdnOptionAddress:RadioButton
        var tvFullName:TextView
        var tvNumberPhone:TextView
        var tvAddress:TextView
        var tvAddressDetails:TextView

        init {
            rdnOptionAddress = itemView.findViewById(R.id.rdnOptionAddress)
            tvFullName = itemView.findViewById(R.id.tvFullName)
            tvNumberPhone = itemView.findViewById(R.id.tvNumberPhone)
            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvAddressDetails = itemView.findViewById(R.id.tvAddressDetails)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_option_address,parent,false)
        return viewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            holder.tvFullName.text = list[position].fullName
            holder.tvNumberPhone.text = list[position].numberPhone
            holder.tvAddress.text = "${list[position].phuongXa}, ${list[position].quanHuyen}, ${list[position].tinhThanhPho}"
            holder.tvAddressDetails.text = list[position].addressDetails
        }

        holder.rdnOptionAddress.setOnClickListener {

            if(holder.rdnOptionAddress.isChecked){
                onClick.onClickListener(position)
            }

            if(!holder.rdnOptionAddress.isChecked){
                holder.rdnOptionAddress.isChecked = false
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}