package com.developer.cory.Model


class Order{
    val idOrder:String?=null
    var quantity:Int = 0
    var idVoucher:String? = null
    var product:Product?=null

    constructor()

    constructor(product: Product, quantity:Int){
        this.product = product
        this.quantity = quantity
    }
}