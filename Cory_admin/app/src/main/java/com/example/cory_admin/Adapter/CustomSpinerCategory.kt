package com.example.cory_admin.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cory_admin.Model.Category
import com.example.cory_admin.R

class CustomSpinerCategory(private val activity:Activity, val list:List<Category>):ArrayAdapter<Category>(activity,
    R.layout.list_category_in_add_product) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs = activity.layoutInflater
        val rowView = contexs.inflate(R.layout.list_category_in_add_product,parent,false)
        val img = rowView.findViewById<ImageView>(R.id.imgCategory)
        val tvNameCategory = rowView.findViewById<TextView>(R.id.tvNameCategory)

        Glide.with(context).load(list[position].img_url).into(img)
        tvNameCategory.text = list[position].nameCategory

        return rowView
    }
}