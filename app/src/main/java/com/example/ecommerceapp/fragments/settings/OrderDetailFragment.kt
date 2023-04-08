package com.example.ecommerceapp.fragments.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.ecommerceapp.adapters.BillingProductsAdapter
import com.example.ecommerceapp.data.order.OrderStatus
import com.example.ecommerceapp.data.order.getOrderStatus
import com.example.ecommerceapp.databinding.FragmentOrderDetailBinding
import com.example.ecommerceapp.util.VerticalItemDecoration

class OrderDetailFragment : Fragment() {

    private var _binding :FragmentOrderDetailBinding ?=null
    val binding get() = _binding!!
    private val billingProductsAdapter by lazy {
        BillingProductsAdapter()
    }

    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val order = args.order

        binding.apply {
            tvOrderId.text = "order #${order.orderId}"
            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phone
            tvTotalPrice.text = "$ ${order.totalPrice}"

            stepView.setSteps(mutableListOf(
                OrderStatus.Ordered.status ,
                OrderStatus.Confirmed.status,
                OrderStatus.Shipped.status ,
                OrderStatus.Delivered.status
            ))

            val currentOrderState = when(getOrderStatus(order.orderStatus)){
                OrderStatus.Ordered -> 0
                OrderStatus.Confirmed -> 1
                OrderStatus.Shipped -> 2
                OrderStatus.Delivered -> 3
                else ->0
            }

            stepView.go(currentOrderState , false)
            if (currentOrderState==3){
                stepView.done(true)
            }

        }


        billingProductsAdapter.differ.submitList(order.products)


    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
            addItemDecoration(VerticalItemDecoration())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}