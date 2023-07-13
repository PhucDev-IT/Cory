package com.developer.cory.Model

import com.google.type.Date

class User {
    private var _name: String? = null
    var name: String?
        get() = _name
        set(value) {
            _name = value
        }

    private var _dateOfBirth: Date? = null
    var dateOfBirth: Date?
        get() = _dateOfBirth
        set(value) {
            _dateOfBirth = value
        }

    private var _address:String?=null
    var address:String?
        get() = _address
        set(value) {_address = value}

    private var _numberPhone:String?=null
    var numberPhone:String?
        get() = _numberPhone
        set(value) {_numberPhone = value}

    constructor()

    constructor(numberPhone:String){
        this._numberPhone = numberPhone
    }
}