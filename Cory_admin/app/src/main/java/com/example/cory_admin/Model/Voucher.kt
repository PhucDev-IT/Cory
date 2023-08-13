package com.example.cory_admin.Model


import com.google.firebase.Timestamp
import com.google.type.Date

class Voucher() {

    var img_url:String?=null
    var idVoucher:String?=null
    var title:String?=null
    var description:String?=null
    var quantity:Int = 0
    var usedVoucher:Int = 0
    var startTime: Long?=null
    var endTime:Long?=null
    var typeVoucher:String?=null
    var reduce:Double = 0.0
    override fun toString(): String {
        return "Voucher(img_url=$img_url, idVoucher=$idVoucher, title=$title, description=$description, quantity=$quantity, usedVoucher=$usedVoucher, startTime=$startTime, endTime=$endTime, typeVoucher=$typeVoucher, reduce=$reduce)"
    }


}