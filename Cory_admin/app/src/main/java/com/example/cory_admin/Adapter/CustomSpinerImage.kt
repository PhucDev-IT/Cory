package com.example.cory_admin.Adapter

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cory_admin.Model.Category
import com.example.cory_admin.R

class CustomSpinerImage(private val activity:Activity, val list:List<String>):ArrayAdapter<String>(activity,
    R.layout.viewholder_image) {

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
        val rowView = contexs.inflate(R.layout.viewholder_image,parent,false)
        val img = rowView.findViewById<ImageView>(R.id.img)

        Glide.with(context).load(list[position]).into(img)


        return rowView
    }
}