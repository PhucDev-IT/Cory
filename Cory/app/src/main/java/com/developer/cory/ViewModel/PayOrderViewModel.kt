package com.developer.cory.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.developer.cory.Model.Address
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Temp
import com.developer.cory.Model.TypeVoucher
import com.developer.cory.Model.Voucher
import com.developer.cory.Service.AddressService

class PayOrderViewModel : ViewModel() {

    init {
        // reset()
    }

    private val _listCart = MutableLiveData<List<CartModel>>()
    val listCart: LiveData<List<CartModel>> = _listCart

    private val _voucher = MutableLiveData<Voucher>()
    val voucher: LiveData<Voucher> = _voucher

    private val _mAddress = MutableLiveData<Address>()
    val mAddress: LiveData<Address> = _mAddress

    private val _totalMoney = MutableLiveData<Double>()
    val totalMoney: LiveData<Double> = _totalMoney

    private val _tongPhiVanChuyen = MutableLiveData(0.0)
    val tongPhiVanChuyen: LiveData<Double> = _tongPhiVanChuyen

    private var _giamGiaVanChuyen = MutableLiveData(0.0)
    val giamGiaVanChuyen: LiveData<Double> = _giamGiaVanChuyen

    private var _voucherGiamGia = MutableLiveData(0.0)
    val voucherGiamGia: LiveData<Double> = _voucherGiamGia

    private var _giamGiaXu = MutableLiveData(0)
    val giamGiaXu: LiveData<Int> = _giamGiaXu

    private val _tongThanhToan = MutableLiveData(0.0)
    val tongThanhToan: LiveData<Double> = _tongThanhToan

    fun setTotalMoney(value: Double) {
        _totalMoney.value = value
    }

    fun setListCart(list: List<CartModel>) {
        _listCart.value = list
    }

    fun setAddress(address: Address) {
        _mAddress.value = address
    }

    fun setTongPhiVanChuyen(tongVanChuyen: Double) {
        _tongPhiVanChuyen.value = tongVanChuyen
    }

    fun setGiamGiaVanChuyen(giamGiaVanChuyen: Double) {
        _giamGiaVanChuyen.value = giamGiaVanChuyen
    }

    fun setVoucherGiamGia(voucherGiamGia: Double) {
        _voucherGiamGia.value = voucherGiamGia
    }

    fun setGiamGiaXu(giamXu: Int) {
        _giamGiaXu.value = giamXu
    }

    private fun reset() {
        _listCart.value = emptyList()
        _totalMoney.value = 0.0
    }

    fun tinhTongTienThanhToan() {
        _tongThanhToan.value = (totalMoney.value?.plus(tongPhiVanChuyen.value!!)
            ?: 0.0) + giamGiaVanChuyen.value!! + voucherGiamGia.value!! + giamGiaXu.value!!
    }

    fun setVoucher(voucher: Voucher) {
        _giamGiaVanChuyen.value = 0.0
        _voucherGiamGia.value = 0.0

        if (voucher.typeVoucher == TypeVoucher.FREESHIP.name) {
            _giamGiaVanChuyen.value = tongPhiVanChuyen.value?.unaryMinus()

        } else if (voucher.typeVoucher == TypeVoucher.GIAMTHEOTIEN.name) {
            _voucherGiamGia.value = voucher.reduce.unaryMinus()

        } else {
            _voucherGiamGia.value = tongThanhToan.value
        }

        tinhTongTienThanhToan()
    }


    fun setDefaultAddress(addressService: AddressService) {

        if (_mAddress.value != null) {
            return
        }
        Temp.user?.id?.let {
            addressService.selectByIDUser(it) { listAddress ->
                for (address in listAddress) {
                    Log.w(TAG, "${address}")
                    if (address.isDefault == true) {
                        _mAddress.value = address
                        break
                    }
                }
            }
        }
    }


}