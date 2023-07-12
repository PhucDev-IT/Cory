package com.developer.cory.Model

import java.io.Serializable

class Product : Serializable {
    private var _id: String? = null
    val id: String?
        get() = _id


    private var _nameProduct: String? = null
    var name: String?
        get() = _nameProduct
        set(value) {
            _nameProduct = value
        }

    private var _img_url: String? = null
    var img_url: String?
        get() = _img_url
        set(value) {
            _img_url = value
        }

    private var _price: Double? = null
    var price: Double?
        get() = _price
        set(value) {
            _price = value
        }

    private var _description:String?=null
    var description:String?
        get() = _description
        set(value) { _description = value}


    private var _listSize:MutableMap<String,Double>?=null
    var listSize:MutableMap<String,Double>?
        get() = _listSize
        set(value) {_listSize = value}

    private var _classify:String?=null
    var classify:String?
        get() = _classify
        set(value) {_classify = value}

    private var _sideDishes:Map<String,Double>?=null
    var sideDishes:Map<String,Double>?
        get() = _sideDishes
        set(value) {_sideDishes = value}

    constructor()
    constructor(nameProduct: String,price: Double) {
        this.name = nameProduct
        this.price = price
    }


    override fun toString(): String {
        return "Product(_id=$_id, _nameProduct=$_nameProduct, _img_url=$_img_url, _price=$_price, _description=$_description, _listSize=$_listSize, _classify=$_classify)"
    }


}