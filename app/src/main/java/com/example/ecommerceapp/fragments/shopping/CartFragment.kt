package com.example.ecommerceapp.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.CartProductsAdapter
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.firebase.FirebaseCommon
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.VerticalItemDecoration
import com.example.ecommerceapp.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding : FragmentCartBinding ?=null
    val binding get() = _binding!!
    private val viewModel : CartViewModel by activityViewModels()
    private val cartAdapter by lazy { CartProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()

        lifecycleScope.launch{
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Unspecified -> Unit
                }
            }

        }

        lifecycleScope.launch {
            viewModel.productsPrice.collectLatest {price->
                price?.let {
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteProduct.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Item from Cart")
                    setMessage("Do you want to delete this item from cart ? ")
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){dialog,_->
                        viewModel.deleteProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        cartAdapter.onProductClick ={
            val b = Bundle().apply { putParcelable("product" , it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment , b )
        }

        cartAdapter.onPlusClick ={
            viewModel.changeQuantity(FirebaseCommon.QuantityChanging.INCREASE , it)
        }
        cartAdapter.onMinusClick ={
            viewModel.changeQuantity(FirebaseCommon.QuantityChanging.DECREASE , it)
        }


    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }

    }

    private fun hideEmptyCart() {
        binding.layoutCarEmpty.visibility = View.GONE
    }

    private fun showEmptyCart() {
        binding.layoutCarEmpty.visibility = View.VISIBLE
    }

    private fun setupRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}