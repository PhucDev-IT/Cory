package com.example.cory_admin.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory(): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Create a ViewModel for the current Fragment
        return OrdersManagerViewModel() as T
    }
}