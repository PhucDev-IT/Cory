package com.example.cory_admin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cory_admin.Interface.RvInterface
import com.example.cory_admin.Model.Category
import com.example.cory_admin.R


class RvCategoryAdapter(private val list: List<Category>, private val onClick: RvInterface) :
    RecyclerView.Adapter<RvCategoryAdapter.viewHolder>() {


    class viewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_category, parent, false)
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