package com.example.cory_admin.Adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cory_admin.Model.TypeVoucher
import com.example.cory_admin.Model.Voucher
import com.example.cory_admin.R

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.reflect.typeOf

class RvVoucherAdapter() :
    RecyclerView.Adapter<RvVoucherAdapter.viewHolder>() {
    private var list: MutableList<Voucher> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(lst:List<Voucher>){
        list.addAll(lst)
        notifyDataSetChanged()
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgVoucher: ImageView
        var idVoucher: TextView
        var tvTitle: TextView
        var tvDescription: TextView
        var tvQuantity: TextView
        var tvTimeUse: TextView

        init {
            imgVoucher = itemView.findViewById(R.id.imgVoucher)
            idVoucher = itemView.findViewById(R.id.idVoucher)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)
            tvTimeUse = itemView.findViewById(R.id.tvTimeUse)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_voucher, parent, false)
        return viewHolder(view)
    }

    // Định dạng thời gian
    @SuppressLint("SimpleDateFormat")
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            holder.idVoucher.text = list[position].idVoucher
            Glide.with(context).load(list[position].img_url).into(holder.imgVoucher)
            holder.tvDescription.text = list[position].description
            holder.tvTitle.text = list[position].title

            val date = list[position].endTime?.let { Date(it) }
            val endTime = date?.let { dateFormat.format(it) }
            val currentTime = System.currentTimeMillis()
            val difference = currentTime - list[position].endTime!!

            val hours = TimeUnit.MILLISECONDS.toHours(difference)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(difference) - hours * 60

            if (difference < 0) {
                holder.tvTimeUse.text = "Kết thúc: $endTime"
            } else if (hours >= 1) {
                holder.tvTimeUse.text = "Sắp hết hạn: còn $hours giờ"
            } else {
                holder.tvTimeUse.text = "Sắp hết hạn: còn $minutes phút"
            }


            holder.tvQuantity.text =
                (list[position].quantity - list[position].usedVoucher).toString()

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}