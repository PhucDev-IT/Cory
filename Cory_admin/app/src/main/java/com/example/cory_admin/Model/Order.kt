package com.example.cory_admin.Model



import java.io.Serializable
import java.util.Date


class Order() : Serializable{

    private var _idOrder:String?=null
    var idOrder:String?
        get() = _idOrder
        set(value) {_idOrder = value}

    private var _voucher:Voucher?=null
    var voucher:Voucher?
        get() = _voucher
        set(value) {_voucher = value}

    private var _listCart:List<CartModel>?=null
    var listCart: List<CartModel>?
        get() = _listCart
        set(value) {_listCart = value}

    private var _usedXu:Int = 0
    var usedXu:Int?
        get() = _usedXu
        set(value) {_usedXu = value?:0}

    private var _tongPhiVanChuyen:Double =0.0
    var tongPhiVanChuyen:Double?
        get() = _tongPhiVanChuyen
        set(value) {_tongPhiVanChuyen = value?:0.0}

    private var _mAddress:Address?=null
    var mAddress:Address?
        get() = _mAddress
        set(value) {_mAddress = value}

    private var _tongTienSanPham:Double = 0.0
    var tongTienSanPham:Double?
        get() = _tongTienSanPham
        set(value) {_tongTienSanPham = value?:0.0}

    private var _phuongThucThanhToan:String?=null
    var phuongThucThanhToan:String?
        get() = _phuongThucThanhToan
        set(value) {_phuongThucThanhToan = value}

    private var _status:String = "Chờ xác nhận"
    var status:String?
        get() = _status
        set(value) {_status = value?:"Chờ xác nhận"}

    private var _orderDate: Date?=null
    var orderDate:Date?
        get() = _orderDate
        set(value) {_orderDate = value}


    override fun toString(): String {
        return "Order(_idOrder=$_idOrder, _voucher=$_voucher, _listCart=$_listCart, _usedXu=$_usedXu, _tongPhiVanChuyen=$_tongPhiVanChuyen, _mAddress=$_mAddress, _tongTienSanPham=$_tongTienSanPham, _phuongThucThanhToan=$_phuongThucThanhToan, _status='$_status', _orderDate=$_orderDate)"
    }


}