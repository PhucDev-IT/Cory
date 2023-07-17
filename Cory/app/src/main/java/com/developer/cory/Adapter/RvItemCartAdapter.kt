package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.FormatCurrency.Companion.numberFormat
import com.developer.cory.R

class RvItemCartAdapter(private val list: List<CartModel>, private val onClick:RvPriceInterface) :
    RecyclerView.Adapter<RvItemCartAdapter.viewHolder>() {


    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameProduct: TextView
        var tvClassify: TextView
        var tvPrice: TextView
        var tvQuantity: TextView
        var imgProduct: ImageView
        var ckbProduct: CheckBox
        var tvSideDishes: TextView
        var btnUp:ImageButton
        var btnDown:ImageButton

        init {
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct)
            tvClassify = itemView.findViewById(R.id.tvClassify)
            tvPrice = itemView.findViewById(R.id.tvPrice)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)
            imgProduct = itemView.findViewById(R.id.imgProduct)
            ckbProduct = itemView.findViewById(R.id.ckbProduct)
            tvSideDishes = itemView.findViewById(R.id.tvSideDishes)
            btnDown = itemView.findViewById(R.id.btnDown)
            btnUp = itemView.findViewById(R.id.btnUp)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholer_item_cart, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    /*
    Ban đầu tính tổng tiền product = số lượng trong giỏ hàng * số tiền hiện tại của product
    -Kiểm tra nếu phân loại trùng với phân loại hiện có của product thì hiển thị và tính tiền
    (tránh không đồng bộ dữ liệu, sp bị thay đổi)
    - Các món ăn phụ cũng sẽ kiểm tra kĩ càng và tính tiền
     */

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var sideDishes: String = ""
        holder.itemView.apply {
             var sumMoney: Double = 0.0
            holder.tvNameProduct.text = list[position].product!!.name
            holder.tvQuantity.text = list[position].quantity.toString()

            sumMoney += (list[position].quantity.toDouble() * list[position].product?.price!!)


            if (list[position].product!!.listSize?.contains(list[position].classify) == true) {
                val price = list[position].product?.listSize?.get(list[position].classify)
                if (price != null) {
                    sumMoney = (price * list[position].quantity)
                }
                holder.tvClassify.text =
                    resources.getString(R.string.str_classify, list[position].classify)
            } else {
                holder.tvClassify.visibility = View.GONE
            }

            if ((list[position].sideDishes?.size ?: 0) > 0) {
                for (key in list[position].sideDishes!!) {
                    if (list[position].product?.sideDishes?.contains(key) == true) {
                        val price = list[position].product?.sideDishes?.get(key)
                        sumMoney += price!!
                        sideDishes += "$key, "
                    }
                }
                holder.tvSideDishes.visibility = View.VISIBLE
            }

            holder.tvSideDishes.text = "Thêm: " + sideDishes
            holder.tvPrice.text = numberFormat.format(sumMoney)
            Glide.with(context).load(list[position].product!!.img_url).into(holder.imgProduct)


            holder.ckbProduct.setOnClickListener{
                val parsedDouble = numberFormat.parse(holder.tvPrice.text.toString())?.toDouble() ?: 0.0
                if (holder.ckbProduct.isChecked) {
                    onClick.onClickListener(parsedDouble,position)
                }else{
                    onClick.onClickListener(parsedDouble.unaryMinus(),position)
                }
            }

        }

        holder.btnUp.setOnClickListener {
            upDownQuantity(holder.btnUp,holder,position)
        }
        holder.btnDown.setOnClickListener {
            upDownQuantity(holder.btnDown,holder,position)
        }
    }
    private fun upDownQuantity(view:ImageButton,holder: viewHolder,position:Int){
        var price = list[position].quantity * list[position].product?.price!!
        var numberBuyProduct = list[position].quantity
        if(view.id == R.id.btnUp){
            numberBuyProduct++
        }else if(view.id == R.id.btnDown && numberBuyProduct>1){
            numberBuyProduct--
        }
        holder.tvQuantity.text = numberBuyProduct.toString()


    }

}