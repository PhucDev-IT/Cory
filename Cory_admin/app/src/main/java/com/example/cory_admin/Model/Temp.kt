package com.example.cory_admin.Model

class Temp {
    companion object{
        var user:User?=null
         var account: Account?=null

        fun reset(){
            user = null
            account = null
        }
    }
}