package com.example.ecommerceapp.data

sealed class Category(val category : String)  {
    object Chair     : Category("Chair")
    object Cupboard  : Category("Cupboard")
    object Table     : Category("Table")
    object Furniture : Category("Furniture")
    object Accessory : Category("Accessory")
}
