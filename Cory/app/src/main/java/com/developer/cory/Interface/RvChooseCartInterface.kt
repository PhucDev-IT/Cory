package com.developer.cory.Interface

import com.developer.cory.Model.CartModel

interface RvChooseCartInterface {
    fun onClickListener(price:Double,cart:CartModel)
}