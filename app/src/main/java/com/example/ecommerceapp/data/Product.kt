package com.example.ecommerceapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
) : Parcelable {
    //we make this constructor because when we retrieve this products
    // and when use toObject function that comes with firestore that function require to have empty constructor
    constructor() : this("0", "", "", 0f, images = emptyList())
}