package com.example.ecommerceapp.util

import android.util.Patterns


fun validateEmail(email : String) : RegisterValidation{

    if(email.isEmpty()){
        return RegisterValidation.Failed("Email Cannot be empty")
    }

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Enter a correct format")
    }
    return RegisterValidation.Success
}

fun validatePassword(password : String):RegisterValidation{

    if(password.isEmpty()){
        return RegisterValidation.Failed("Password Cannot be empty")
    }
    if(password.length<6){
        return RegisterValidation.Failed("You should enter more than 8 charter")
    }
    return RegisterValidation.Success
}