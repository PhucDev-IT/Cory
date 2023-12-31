package com.developer.cory.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Interface.RvInterface

import com.developer.cory.Model.Voucher
import com.developer.cory.R
import java.text.SimpleDateFormat
import java.util.Date

import java.util.concurrent.TimeUnit

class RvOptionVouchersAdapter(private val list: List<Voucher>, private val onClick: RvInterface) :
    RecyclerView.Adapter<RvOptionVouchersAdapter.viewHolder>() {

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgVoucher: ImageView
        var idVoucher: TextView
        var tvTitle: TextView
        var tvDescription: TextView
        var tvQuantity: TextView
        var tvTimeUse: TextView
        var rdnButton:RadioButton

        init {
            imgVoucher = itemView.findViewById(R.id.imgVoucher)
            idVoucher = itemView.findViewById(R.id.idVoucher)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)
            tvTimeUse = itemView.findViewById(R.id.tvTimeUse)
            rdnButton = itemView.findViewById(R.id.rdnButton)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_option_voucher, parent, false)
        return viewHolder(view)
    }

    private var checkedPosition = -1
    // Định dạng thời gian
    @SuppressLint("SimpleDateFormat")
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

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


            // Thiết lập giá trị isChecked theo vị trí của đối tượng trong danh sách
            holder.rdnButton.isChecked = checkedPosition == position;

        }
        holder.rdnButton.setOnClickListener {
            // Cập nhật trạng thái của radioButton được chọn
            checkedPosition = position;
            notifyDataSetChanged(); // Cập nhật giao diện của RecyclerView
            onClick.onClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}