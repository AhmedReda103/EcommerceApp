package com.example.ecommerceapp.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.AddressAdapter
import com.example.ecommerceapp.adapters.BillingProductsAdapter
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.data.order.Order
import com.example.ecommerceapp.data.order.OrderStatus
import com.example.ecommerceapp.databinding.FragmentBillingBinding
import com.example.ecommerceapp.util.HorizontalItemDecoration
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.VerticalItemDecoration
import com.example.ecommerceapp.viewmodel.BillingViewModel
import com.example.ecommerceapp.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private var _binding : FragmentBillingBinding ?=null
    private val binding get() = _binding!!

    private val billingProductsAdapter by lazy {
        BillingProductsAdapter()
    }
    private val addressAdapter by lazy {
        AddressAdapter()
    }

    private val billingViewModel by viewModels<BillingViewModel>()
    private val orderViewModel by viewModels<OrderViewModel>()

    private var selectedAddress:Address ?=null

    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products= args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBillingAdapter()
        setupAddressAdapter()

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        addressAdapter.differ.submitList(it.data)
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ ${totalPrice}"


        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView() ,"Your Order Was Placed " ,Snackbar.LENGTH_SHORT ).show()
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }


        addressAdapter.onItemClick = {
            selectedAddress = it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress==null){
                Toast.makeText(requireContext() , "Please Select an Address" , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showConfirmationDialog()
        }



    }

    private fun showConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Items")
            setMessage("Do you want to order your cart items ? ")
            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){dialog,_->
                val order = Order( OrderStatus.Ordered.status, totalPrice , products , selectedAddress!!)

                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressAdapter() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingAdapter() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}