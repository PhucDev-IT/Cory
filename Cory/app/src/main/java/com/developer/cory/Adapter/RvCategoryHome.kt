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

class RvCategoryHome(private val list:List<Category>,private val onClick: RvInterface):RecyclerView.Adapter<RvCategoryHome.viewHolder>() {

    class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgCategory:ImageView
        val nameCategory:TextView

        init {
            imgCategory = itemView.findViewById(R.id.imgCategoryHome)
            nameCategory = itemView.findViewById(R.id.tvNameCategory)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.itemView.apply {

           Glide.with(context).load(list[position].img_url).into(holder.imgCategory)
           holder.nameCategory.text = list[position].nameCategory

           holder.itemView.setOnClickListener {
               onClick.onClickListener(position)
           }
       }
    }
}