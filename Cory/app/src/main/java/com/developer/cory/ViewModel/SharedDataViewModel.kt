package com.developer.cory.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedDataViewModel<T> :ViewModel() {
    var data =  MutableLiveData<T>()

}