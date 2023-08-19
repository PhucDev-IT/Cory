package com.example.cory_admin.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cory_admin.Service.OrdersService

class OrdersManagerViewModel: ViewModel(){

    private var ordersService = OrdersService()

    private var _listOrdersChoXacNhan = MutableLiveData<List<com.example.cory_admin.Model.Order>>()
    var listOrdersChoXacNhan:LiveData<List<com.example.cory_admin.Model.Order>> = _listOrdersChoXacNhan

    private var _listOrdersDangChoXuLy= MutableLiveData<List<com.example.cory_admin.Model.Order>>()
    var listOrdersDangChoXuLy:LiveData<List<com.example.cory_admin.Model.Order>> = _listOrdersDangChoXuLy


    fun getListOrderChoXacNhan(){
        ordersService.getAllOrderChoXacNhan { list ->
            Log.w(TAG,"MÓE: ${list.size}" )
            _listOrdersChoXacNhan.value = list
        }
    }

    fun getListOrderDangChoXuLy(){
        ordersService.getAllOrderDangChoXuLy { list ->
            _listOrdersDangChoXuLy.value = list
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Dispose All your Subscriptions to avoid memory leaks
    }
}