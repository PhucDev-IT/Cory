package com.developer.cory.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer.cory.Model.Order

class PurchaseHistoryViewModel : ViewModel() {
    private var _mListOrderChoXacNhan =  MutableLiveData<List<Order>>()
    val mListOrderChoXacNhan: LiveData<List<Order>> = _mListOrderChoXacNhan

    private var _mListOrderDangGiao =  MutableLiveData<List<Order>>()
    val mListOrderDangGiao: LiveData<List<Order>> = _mListOrderChoXacNhan

    private var _mListOrderDaMua =  MutableLiveData<List<Order>>()
    val mListOrderDaMua: LiveData<List<Order>> = _mListOrderChoXacNhan

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading:LiveData<Boolean> = _isLoading

    private var _isLastPage = MutableLiveData<Boolean>(false)
    val isLastPage:LiveData<Boolean> = _isLastPage


    fun setListChoXacNhan(values: List<Order>){

        _mListOrderChoXacNhan.value = values
        Log.w(TAG,"Size: ${_mListOrderChoXacNhan.value?.size}")
    }

    fun setListDangGiao(values: List<Order>){
        _mListOrderDangGiao.value = values
    }

    fun setListDaMuaHang(values: List<Order>){
        _mListOrderDaMua.value = values
    }

    fun setIsLoading(value:Boolean){
        _isLoading.value = value
    }

    fun setIsLastPage(value:Boolean){
        _isLastPage.value = value
    }
}