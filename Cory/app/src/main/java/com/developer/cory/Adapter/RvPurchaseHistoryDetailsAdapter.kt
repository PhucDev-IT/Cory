package com.developer.cory.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Order
import com.developer.cory.R
import java.text.SimpleDateFormat

class RvPurchaseHistoryDetailsAdapter(private val list: List<CartModel>) :
    RecyclerView.Adapter<RvPurchaseHistoryDetailsAdapter.viewHolder>() {
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView
        val tvNameProduct: TextView
        val tvClassify: TextView
        val tvSideDishes: TextView
        val tvPrice: TextView
        val tvNumberBuyProduct: TextView
        val tvTotalMoney: TextView

        init {
            imgProduct = itemView.findViewById(R.id.imgProduct)
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct)
            tvClassify = itemView.findViewById(R.id.tvClassify)
            tvSideDishes = itemView.findViewById(R.id.tvSideDishes)

            tvPrice = itemView.findViewById(R.id.tvPrice)
            tvNumberBuyProduct = itemView.findViewById(R.id.tvNumberBuyProduct)
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_purchase_history_details, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {

            Glide.with(context).load(list[position].product?.img_url).into(holder.imgProduct)
            holder.tvNameProduct.text = list[position].product?.name
            holder.tvPrice.text = FormatCurrency.numberFormat.format(list[position].product?.price)
            holder.tvNumberBuyProduct.text = "${list[position].quantity} sản phẩm"
            holder.tvTotalMoney.text = FormatCurrency.numberFormat.format(list[position].totalMoney)
            if (list[position].classify != null) {
                holder.tvClassify.visibility = View.VISIBLE
                holder.tvClassify.text = list[position].classify
            }

            if (list[position].sideDishes != null) {
                var sideDishes: String = ""
                for (str in list[position].sideDishes!!) {
                    sideDishes += "$str, "
                }

                holder.tvSideDishes.visibility = View.VISIBLE
                holder.tvSideDishes.text = "Thêm: $sideDishes"
            }


        }


    }
}