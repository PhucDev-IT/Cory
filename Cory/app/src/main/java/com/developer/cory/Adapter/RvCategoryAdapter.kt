package com.developer.cory.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cory.Model.Category
import com.developer.cory.Interface.RvInterface
import com.developer.cory.R


class RvCategoryAdapter(private val list: List<Category>, private val onClick: RvInterface, var pos:Int) :
    RecyclerView.Adapter<RvCategoryAdapter.viewHolder>() {


    class viewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_category_search, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var holderTemp: viewHolder? = null
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            val img = findViewById<ImageView>(R.id.imgCategory)
            val name = findViewById<TextView>(R.id.nameCategory)

            Glide.with(context).load(list[position].img_url).into(img)
            name.text = list[position].nameCategory

            //Xử lý khi người dùng chọn 1 category bất kỳ ở home activity
            if(pos>=0 && pos == position){
                holder.itemView.setBackgroundResource(R.drawable.category_selected)
                holderTemp = holder
                onClick.onClickListener(pos)

            }else{
                holder.itemView.setBackgroundResource(0)
            }

            //Lắng nghe sự kiện người dùng
            holder.itemView.setOnClickListener {
                if (holder != holderTemp) {
                    holderTemp?.itemView?.setBackgroundResource(R.drawable.category_nomal)
                    holder.itemView.setBackgroundResource(R.drawable.category_selected)
                    holderTemp = holder
                    onClick.onClickListener(position)
                }
            }
        }
    }
}