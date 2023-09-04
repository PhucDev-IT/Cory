package com.developer.cory.Model

import android.text.TextUtils

class Account {
    var numberPhone:String ?= null
    var password:String?= null
    var phanQuyen:String ?= null
    var numberXu:Int = 0
    var email:String?=null

    constructor()

    constructor(number: String,password:String){
        this.numberPhone = number
        this.password = password
    }

    constructor(numberPhone: String?,email:String?, password: String?) {
        this.numberPhone = numberPhone
        this.password = password
        this.phanQuyen = "Customer"
        this.email = email
    }


}