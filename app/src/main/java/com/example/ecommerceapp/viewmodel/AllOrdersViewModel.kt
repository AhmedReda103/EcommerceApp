package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ecommerceapp.data.order.Order
import com.example.ecommerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore ,
    private val firebaseAuth: FirebaseAuth
):ViewModel(){

    private val _orders = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
     val orders = _orders.asStateFlow()


    init {
        getAllOrders()
    }


    private fun getAllOrders() {
        viewModelScope.launch { _orders.emit(Resource.Loading()) }

        firestore.collection("user").document(firebaseAuth.uid!!).collection("orders").get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                viewModelScope.launch {_orders.emit(Resource.Success(orders))}
            }.addOnFailureListener {
                viewModelScope.launch {_orders.emit(Resource.Error(it.message.toString()))}
            }
    }


}