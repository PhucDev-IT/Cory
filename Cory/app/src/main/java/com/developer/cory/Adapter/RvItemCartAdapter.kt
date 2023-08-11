package com.developer.cory.Adapter


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Interface.CheckboxInterface
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.FormatCurrency.Companion.numberFormat
import com.developer.cory.R
import com.developer.cory.Service.CartService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RvItemCartAdapter(private var list: List<CartModel>, private val onClick: RvPriceInterface,
private val onCheckbox:CheckboxInterface) :
    RecyclerView.Adapter<RvItemCartAdapter.viewHolder>() {

    fun setData(list:List<CartModel>){
        this.list = list
        notifyDataSetChanged()
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameProduct: TextView
        var tvClassify: TextView
        var tvPrice: TextView
        var tvQuantity: TextView
        var imgProduct: ImageView
        var ckbProduct: CheckBox
        var tvSideDishes: TextView
        var btnUp: ImageButton
        var btnDown: ImageButton

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
        var sideDishes = ""
        holder.itemView.apply {

            val cartM = list[position]

            holder.tvNameProduct.text = cartM.product?.name
            holder.tvQuantity.text = cartM.quantity.toString()

            cartM.totalMoney += (cartM.quantity.toDouble() * cartM.product?.price!!)


            if (cartM.product!!.listSize?.contains(cartM.classify) == true) {
                val price = cartM.product?.listSize?.get(cartM.classify)
                if (price != null) {
                    cartM.totalMoney = (price * cartM.quantity)
                }

                holder.tvClassify.text =
                    resources.getString(R.string.str_classify, cartM.classify)
            } else {
                holder.tvClassify.visibility = View.GONE
            }

            if ((cartM.sideDishes?.size ?: 0) > 0) {
                for (key in cartM.sideDishes!!) {
                    if (cartM.product?.sideDishes?.contains(key) == true) {
                        val price = cartM.product?.sideDishes?.get(key)
                        cartM.totalMoney += price!!
                        sideDishes += "$key, "
                    }
                }
                holder.tvSideDishes.visibility = View.VISIBLE
            }

            holder.tvSideDishes.text = "Thêm: $sideDishes "
            holder.tvPrice.text = numberFormat.format(cartM.totalMoney)
            Glide.with(context).load(list[position].product!!.img_url).into(holder.imgProduct)

            holder.ckbProduct.setOnClickListener {

                if (holder.ckbProduct.isChecked) {
                    onCheckbox.isChecked(position)
                } else {
                    onCheckbox.isNotChecked(position)
                }
            }

        }

        holder.btnUp.setOnClickListener {
           upDownQuantity(holder.btnUp, holder, position)
            if(holder.ckbProduct.isChecked){
                onClick.onClickListener(list[position].totalMoney, position)
            }

        }
        holder.btnDown.setOnClickListener {
            upDownQuantity(holder.btnDown, holder, position)
            if(holder.ckbProduct.isChecked) {
                onClick.onClickListener(list[position].totalMoney.unaryMinus(), position)
            }
        }

    }

    val db = Firebase.firestore
    private fun upDownQuantity(view: ImageButton, holder: viewHolder, position: Int) {

        var numberBuyProduct = list[position].quantity

        if (view.id == R.id.btnUp) {
            numberBuyProduct++
        } else if (view.id == R.id.btnDown && numberBuyProduct > 1) {
            numberBuyProduct--
        }

        val sumMoneyAfterChange = (numberBuyProduct - list[position].quantity ) * list[position].product?.price!!

        list[position].totalMoney += sumMoneyAfterChange
        list[position].quantity = numberBuyProduct

        holder.tvPrice.text = numberFormat.format(list[position].totalMoney)

        holder.tvQuantity.text = numberBuyProduct.toString()

        db.collection("Cart").document("l3vFMy0dOKaaxHfNcnV1").collection("ItemsCart")
            .document(list[position].product?.id!!)
            .update("quantity",numberBuyProduct)
            .addOnFailureListener { E->
                Log.e(TAG,"Có lỗi: ",E)
            }

    }

}