package com.developer.cory.Model

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Account {
    @PrimaryKey
    lateinit var userName:String
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