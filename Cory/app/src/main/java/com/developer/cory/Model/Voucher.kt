package com.developer.cory.Model

import com.google.type.Date

class Voucher {

    var img_url:String?=null
    var idVoucher:String?=null
    var description:String?=null
    var quantity:Int = 0
    var usedVoucher:Int = 0
    var timeUse:Date?=null
    var minMoney:Double = 0.0       //Sử dụng cho đơn từ bn - bn
    var maxMoney:Double = 0.0
    var typeVoucher:String?=null
    var reduce:Double = 0.0

    constructor()
}