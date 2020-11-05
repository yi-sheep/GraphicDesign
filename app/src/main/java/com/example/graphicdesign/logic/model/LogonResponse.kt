package com.example.graphicdesign.logic.model

data class LogonResponse(val result:Boolean,val user:User)
data class User(val account:String,val password:String,val token:String)