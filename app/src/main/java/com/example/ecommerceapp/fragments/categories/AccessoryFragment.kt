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
class AccessoryFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore , Category.Accessory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest{
                when(it){
                    is Resource.Success->{
                        offerProductAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    }
                    is Resource.Error->{
                        Snackbar.make(requireView() , it.message.toString() , Snackbar.LENGTH_LONG).show()
                        hideOfferLoading()
                    }
                    is Resource.Loading->{
                        showOfferLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest{
                when(it){
                    is Resource.Success->{
                        bestProductAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()
                    }
                    is Resource.Error->{
                        Snackbar.make(requireView() , it.message.toString() , Snackbar.LENGTH_LONG).show()
                        hideBestProductsLoading()
                    }
                    is Resource.Loading->{
                        showBestProductsLoading()
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onOfferPagingRequest() {
        super.onOfferPagingRequest()
    }

    override fun onBestProductPagingRequest() {
        super.onBestProductPagingRequest()
    }


}