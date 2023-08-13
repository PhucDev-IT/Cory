package com.example.cory_admin.Model

import com.google.firebase.firestore.Exclude
import com.google.type.Date

class User {

    private var _id:String?=null
    var id:String?
        get() = _id
        set(value) {_id = value}

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

    @Exclude
    private var _listAddress:List<Address>?=null
    var listAddress:List<Address>?
        get() = _listAddress
        set(value) {_listAddress = value}


    private var _numberPhone:String?=null
    var numberPhone:String?
        get() = _numberPhone
        set(value) {_numberPhone = value}

    constructor()

    constructor(numberPhone:String){
        this._numberPhone = numberPhone
    }



}