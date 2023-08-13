package com.example.cory_admin.Model

import java.io.Serializable

class Product : Serializable {
    private var _id: String? = null
    var id: String?
        get() = _id
        set(value) {_id  = value}

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


    private var _classify:Map<String,Double>?=null
    var classify:Map<String,Double>??
        get() = _classify
        set(value) {_classify = value}

    private var _sideDishes:Map<String,Double>?=null
    var sideDishes:Map<String,Double>?
        get() = _sideDishes
        set(value) {_sideDishes = value}

    private var _idCategory:String?=null
    var idCategory:String?
        get() = _idCategory
        set(value) {_idCategory = value}

    constructor()
    constructor(nameProduct: String,price: Double) {
        this.name = nameProduct
        this.price = price
    }

    override fun toString(): String {
        return "Product(_id=$_id, _nameProduct=$_nameProduct, _img_url=$_img_url, _price=$_price, _description=$_description, _classify=$_classify, _sideDishes=$_sideDishes, _idCategory=$_idCategory)"
    }


}