package com.example.ecommerceapp.data.order

import android.os.Parcelable
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus : String ="",
    val totalPrice : Float=0f,
    val products : List<CartProduct> = emptyList(),
    val address:Address = Address(),
    val date :String = SimpleDateFormat("yyyy-mm-dd" , Locale.ENGLISH ).format(Date()),
    val orderId :Long = nextLong(0 , 100_000_000_000) + totalPrice.toLong()
):Parcelable
