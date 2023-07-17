package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.R
import java.text.NumberFormat
import java.util.Locale

class RvCheckBSideDishes(private val list:List<Map.Entry<String,Double>>,private val onClick: RvPriceInterface)
    :RecyclerView.Adapter<RvCheckBSideDishes.viewHolder>(){

    class viewHolder(itView:View):RecyclerView.ViewHolder(itView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkb_side_dishes_background,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            val ckbNameDishes = findViewById<CheckBox>(R.id.checkboxName)
            val tvPrice = findViewById<TextView>(R.id.tvPrice)

            ckbNameDishes.text = list[position].key
            tvPrice.text = FormatCurrency.numberFormat.format(list[position].value)

            ckbNameDishes.setOnClickListener {
              if(ckbNameDishes.isChecked){
                  onClick.onClickListener(list[position].value,position)
              }else{
                  onClick.onClickListener(list[position].value.unaryMinus(),position)
              }
            }
        }
    }
}