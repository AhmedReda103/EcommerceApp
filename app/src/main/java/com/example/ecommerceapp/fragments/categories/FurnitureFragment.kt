package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.data.Category
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.viewmodel.CategoryViewModel
import com.example.ecommerceapp.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FurnitureFragment : BaseCategoryFragment() {

    private val viewModel by viewModels<CategoryViewModel>{
        BaseCategoryViewModelFactory(firestore , Category.Furniture)
    }

    @Inject lateinit var firestore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when(it){

                    is Resource.Error -> {
                        hideOfferLoading()
                        Snackbar.make(requireView() , it.message.toString() , Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        showOfferLoading()
                    }
                    is Resource.Success -> {
                        hideOfferLoading()
                        offerProductAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){

                    is Resource.Error -> {
                        hideBestProductsLoading()
                        Snackbar.make(requireView() , it.message.toString() , Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        showBestProductsLoading()
                    }
                    is Resource.Success -> {
                        hideBestProductsLoading()
                        offerProductAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onBestProductPagingRequest() {
        super.onBestProductPagingRequest()
    }

    override fun onOfferPagingRequest() {
        super.onOfferPagingRequest()
    }


}