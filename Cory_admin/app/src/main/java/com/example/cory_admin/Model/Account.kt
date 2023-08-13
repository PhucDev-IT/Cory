package com.example.cory_admin.Model

import android.text.TextUtils

class Account {
    var numberPhone:String ?= null
    var password:String?= null
    var phanQuyen:String ?= null
    var numberXu:Int = 0

    constructor()

    constructor(number: String,password:String){
        this.numberPhone = number
        this.password = password
    }

    constructor(numberPhone: String?, password: String?, phanQuyen: String?) {
        this.numberPhone = numberPhone
        this.password = password
        this.phanQuyen = phanQuyen

    }


    fun checkNumberPhone():Boolean{
        return !TextUtils.isEmpty(numberPhone) && numberPhone?.length!! >=10
    }

    fun checkPassword():Boolean
    {
        return !TextUtils.isEmpty(password) && password?.length!! >=6
    }

}