package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Model.TypeVoucher
import com.developer.cory.Model.Voucher
import com.developer.cory.R

class RvVoucherAdapter(private val list:List<Voucher>):RecyclerView.Adapter<RvVoucherAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var imgVoucher:ImageView
        var idVoucher:TextView
        var tvTypeVoucher:TextView
        var tvDescription:TextView
        var tvQuantity:TextView
        var tvTimeUse:TextView

        init {
            imgVoucher = itemView.findViewById(R.id.imgVoucher)
            idVoucher = itemView.findViewById(R.id.idVoucher)
            tvTypeVoucher = itemView.findViewById(R.id.tvTypeVoucher)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)
            tvTimeUse = itemView.findViewById(R.id.tvTimeUse)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_voucher,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.itemView.apply {
           holder.idVoucher.text = list[position].idVoucher

           if(list[position].typeVoucher == TypeVoucher.FREESHIP.name){
               holder.tvTypeVoucher.text = "Miễn phí vận chuyển"
           }else if(list[position].typeVoucher == TypeVoucher.GIAMTHEOPHANTRAM.name)
           {
               holder.tvTypeVoucher.text = "Giảm: ${list[position].reduce}%"
           }else if(list[position].typeVoucher == TypeVoucher.GIAMTHEOTIEN.name)
           {
               holder.tvTypeVoucher.text = "Giảm: ${list[position].reduce}đ"
           }

           holder.tvQuantity.text = (list[position].quantity - list[position].usedVoucher).toString()
       }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}