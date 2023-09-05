package com.developer.cory.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _fullName = MutableLiveData<String>()
    var fullName: LiveData<String> = _fullName

    private val _numberPhone = MutableLiveData<String>()
    var numberPhone: LiveData<String> = _numberPhone

    private val _email = MutableLiveData<String>()
    var email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    var password: LiveData<String> = _password

    private val _verificationId = MutableLiveData<String>()
    var verificationId: LiveData<String> = _verificationId

    private val _isAllowSignUpWithPhone = MutableLiveData(false)
    var isAllowSignUpWithPhone: LiveData<Boolean> = _isAllowSignUpWithPhone

    private val _isAllowSignUpWithEmail = MutableLiveData(false)
    var isAllowSignUpWithEmail: LiveData<Boolean> = _isAllowSignUpWithEmail


    fun setFullName(name: String) {
        _fullName.value = name
    }

    fun setPhone(phone: String) {
        if (phone[0] == '0') {
            if (phone.startsWith('0')) {
                _numberPhone.value = "+84${phone.removeRange(0, 1)}"
            }
        } else {
            _numberPhone.value = "+84$phone"
        }
    }

    fun setEmail(email:String){
        _email.value = email
    }

    fun setPassword(pass: String) {
        _password.value = pass
    }

    fun setVerificationId(pass: String) {
        _verificationId.value = pass
    }

    fun isAllowSignUpWithPhone(b:Boolean){
        _isAllowSignUpWithPhone.value = b
    }

    fun isAllowSignUpWithEmail(b:Boolean){
        _isAllowSignUpWithEmail.value = b
    }
}