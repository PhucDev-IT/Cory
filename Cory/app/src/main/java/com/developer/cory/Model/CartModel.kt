package com.developer.cory.Model

import java.io.Serializable

class CartModel:Serializable{
    var quantity:Long = 0
    var classify:String?=null
    var sideDishes:ArrayList<String>?=null
    var product:Product?=null
    var totalMoney:Double = 0.0

    constructor(
    )

    constructor(quantity: Long, classify: String?, sideDishes: ArrayList<String>?) {
        this.quantity = quantity
        this.classify = classify
        this.sideDishes = sideDishes
    }

    override fun toString(): String {
        return "CartModel(quantity=$quantity, classify=$classify, sideDishes=$sideDishes, product=$product)"
    }


}