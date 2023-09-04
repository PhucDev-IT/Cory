package com.example.cory_admin.Adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cory_admin.Interface.RvInterface
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.Model.FormatCurrency
import com.example.cory_admin.Model.Order
import com.example.cory_admin.R
import java.text.SimpleDateFormat

class OrderManagerAdapter(private val onClick:RvInterface):RecyclerView.Adapter<OrderManagerAdapter.viewHolder>(){
    private var list: MutableList<Order> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lst:List<Order>){
        Log.w(TAG,"List size: ${lst.size}")
        this.list.addAll(lst)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeOrder(order: Order){
        list.remove(order)
        notifyDataSetChanged()
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_item_order, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        holder.itemView.apply {

            holder.tvNgayDatHang.text = list[position].orderDate?.let { sdf.format(it) }

            if(list[position].status == EnumOrder.DANGGIAOHANG.name){
                holder.tvStatus.text = "Đang giao hàng"
            }else if(list[position].status ==  EnumOrder.CHOXACNHAN.name){
                holder.tvStatus.text = "Chờ xác nhận"
            }else if(list[position].status ==  EnumOrder.GIAOHANGTHANHCONG.name){
                holder.tvStatus.text = "Giao hàng thành công"
            }

            if (list[position].listCart != null)
                for (cart in list[position].listCart!!) {
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

        holder.btnXem.setOnClickListener {
            onClick.onClickListener(position)
        }
    }

}