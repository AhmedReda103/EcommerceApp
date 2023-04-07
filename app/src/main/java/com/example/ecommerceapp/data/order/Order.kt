package com.example.ecommerceapp.data.order

import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.CartProduct

data class Order(
    val orderStatus : String ,
    val totalPrice : Float ,
    val products : List<CartProduct>,
    val address:Address
)
