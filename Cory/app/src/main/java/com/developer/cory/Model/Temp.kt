package com.developer.cory.Model

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