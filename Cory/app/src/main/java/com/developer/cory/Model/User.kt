package com.developer.cory.Model

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

    private var _idAccount: String? = null
    var idAccount: String?
        get() = _idAccount
        set(value) {
            _idAccount = value
        }

    @Exclude
    private var _listAddress:List<Address>?=null
    var listAddress:List<Address>?
        get() = _listAddress
        set(value) {_listAddress = value}




    constructor()

    constructor(name:String,idAccount:String){
        this._name = name
        this._idAccount = idAccount
    }



}