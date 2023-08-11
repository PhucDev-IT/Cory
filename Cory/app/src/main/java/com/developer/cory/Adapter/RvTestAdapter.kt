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
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Order
import com.developer.cory.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.text.SimpleDateFormat

class RvTestAdapter(options: FirebaseRecyclerOptions<Order>) : FirebaseRecyclerAdapter<Order, RvTestAdapter.viewHolder>(options) {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView
        val tvNameProduct: TextView
        val tvClassify: TextView
        val tvSideDishes: TextView
        val tvNgayDatHang: TextView
        val tvPrice: TextView
        val tvNumberBuyProduct: TextView
        val tvTotalMoney: TextView
        val btnXem: Button
        val tvStatus: TextView

        init {
            imgProduct = itemView.findViewById(R.id.imgProduct)
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct)
            tvClassify = itemView.findViewById(R.id.tvClassify)
            tvSideDishes = itemView.findViewById(R.id.tvSideDishes)
            tvNgayDatHang = itemView.findViewById(R.id.tvNgayDatHang)
            tvPrice = itemView.findViewById(R.id.tvPrice)
            tvNumberBuyProduct = itemView.findViewById(R.id.tvNumberBuyProduct)
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney)
            btnXem = itemView.findViewById(R.id.btnXem)
            tvStatus = itemView.findViewById(R.id.tvStatus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item_purchased_history,parent,false)
        return viewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: viewHolder, position: Int, model: Order) {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        holder.itemView.apply {
            holder.tvNgayDatHang.text = model.orderDate?.let { sdf.format(it) }

            if(model.status == EnumOrder.DANGGIAOHANG.name){
                holder.tvStatus.text = "Đang giao hàng"
            }else if(model.status ==  EnumOrder.CHOXACNHAN.name){
                holder.tvStatus.text = "Chờ xác nhận"
            }else if(model.status ==  EnumOrder.GIAOHANGTHANHCONG.name){
                holder.tvStatus.text = "Giao hàng thành công"
            }

            if (model.listCart != null)
                for (cart in model.listCart!!) {
                    Glide.with(context).load(cart.product?.img_url).into(holder.imgProduct)
                    holder.tvNameProduct.text = cart.product?.name
                    holder.tvPrice.text = FormatCurrency.numberFormat.format(cart.product?.price)
                    holder.tvNumberBuyProduct.text = "${cart.quantity} sản phẩm"
                    holder.tvTotalMoney.text = FormatCurrency.numberFormat.format(cart.totalMoney)
                    if (cart.classify != null) {
                        holder.tvClassify.visibility = View.VISIBLE
                        holder.tvClassify.text = cart.classify
                    }

                    if (cart.sideDishes != null) {
                        var sideDishes: String = ""
                        for (str in cart.sideDishes!!) {
                            sideDishes += "$str, "
                        }

                        holder.tvSideDishes.visibility = View.VISIBLE
                        holder.tvSideDishes.text = "Thêm: $sideDishes"
                    }
                }
        }


    }

}