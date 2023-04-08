package com.example.ecommerceapp.data.order

sealed class OrderStatus( val status : String) {

    object Ordered : OrderStatus("Ordered")
    object Canceled : OrderStatus("Canceled")
    object Confirmed : OrderStatus("Confirmed")
    object Shipped : OrderStatus("Shipped")
    object Delivered : OrderStatus("Delivered")
    object Returned : OrderStatus("Returned")


}


fun getOrderStatus(status: String):OrderStatus{
    return when(status){
        "Ordered"->{
            return OrderStatus.Ordered
        }
        "Canceled"->{
            return OrderStatus.Canceled
        }
        "Confirmed"->{
            return OrderStatus.Confirmed
        }
        "Shipped"->{
            return OrderStatus.Shipped
        }
        "Delivered"->{
            return OrderStatus.Delivered
        }
        "Returned"->{
            return OrderStatus.Returned
        }
        else -> OrderStatus.Returned
    }
}