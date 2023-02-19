package com.example.ecommerceapp.data

data class User(
    var firstName : String ,
    var secondName :String ,
    var email : String ,
    var imagePath : String =""
){
    constructor():this("","","","")
}