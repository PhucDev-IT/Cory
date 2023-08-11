package com.developer.cory.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer.cory.Model.Product

class SearchActivityViewModel : ViewModel() {
    private var _listProduct = MutableLiveData<List<Product>>()
    var listProduct: LiveData<List<Product>> = _listProduct

    private var _isLoading:Boolean = false
    var isLoading:Boolean
        get() = _isLoading
        set(value) {_isLoading = value}

    private var _isLastPage:Boolean = false
    var isLastPage:Boolean
        get() = _isLastPage
        set(value) {_isLastPage = value}


    fun setListProduct(list:List<Product>){
        isLoading = false
        _listProduct.value = list
    }


}