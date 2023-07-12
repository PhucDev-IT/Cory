package com.developer.cory.Model

import java.io.Serializable


class Category : Serializable{
    val id:String?=null
    var nameCategory:String?=null
    var img_url:String? = null

    constructor()

    override fun toString(): String {
        return "Category(id=$id, nameCategory='$nameCategory', image=$img_url)"
    }
}