package com.developer.cory.Interface

import com.developer.cory.Model.CartModel

interface CkbCartInterface {
    fun isChecked(cart:CartModel)
    fun isNotChecked(cart:CartModel)
}