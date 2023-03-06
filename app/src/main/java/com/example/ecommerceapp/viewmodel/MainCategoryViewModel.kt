package com.example.ecommerceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val firestore: FirebaseFirestore) : ViewModel() {

    private val TAG = "MainCategoryViewModel"

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts = _specialProducts.asStateFlow()

    private val _bestDealsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts = _bestDealsProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestDealsProducts.asStateFlow()

    private val pagingInfo = PagingInfo()

    init {
        fetchBestProducts()
        fetchSpecialProduct()
        fetchBestDealsProducts()
    }

    fun fetchSpecialProduct() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection(COLLECTION_PATH).whereEqualTo(CATEGORY, SPECIAL_PRODUCT).get()
            .addOnSuccessListener { result ->

                val product = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(product))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
        }
    }

    fun fetchBestDealsProducts(){
        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
        }
        firestore.collection(COLLECTION_PATH).whereEqualTo(CATEGORY , BEST_DEALS).get().addOnSuccessListener {
            val product = it.toObjects(Product::class.java)

            Log.d(TAG, "fetchBestDealsProducts: ${product}")


            viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Success(product))
            }
        }.addOnFailureListener {
            viewModelScope.launch{
                _bestDealsProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun fetchBestProducts(){

        if(!pagingInfo.isPageEnd){
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }

            firestore.collection("Products").limit(pagingInfo.page*10).get().addOnSuccessListener {result->
                val bestProducts = result.toObjects(Product::class.java)

                Log.d(TAG, "fetchBestProducts: ${bestProducts}")

                pagingInfo.isPageEnd = pagingInfo.bestOldPages == bestProducts
                pagingInfo.bestOldPages = bestProducts
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProducts))
                }
                pagingInfo.page++

            }.addOnFailureListener {
                viewModelScope.launch{
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }

        }

    }


    companion object{
        const val COLLECTION_PATH = "Products"
        const val CATEGORY = "category"
        const val SPECIAL_PRODUCT = "Special Product"
        const val BEST_DEALS = "Best Deals"
    }

}


data class PagingInfo (
    var page :Long = 1 ,
    var bestOldPages:List<Product> = emptyList() ,
    var isPageEnd : Boolean = false
)