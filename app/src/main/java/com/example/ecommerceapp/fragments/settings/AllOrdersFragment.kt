package com.example.ecommerceapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.adapters.AllOrdersAdapter
import com.example.ecommerceapp.databinding.FragmentOrdersBinding
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.VerticalItemDecoration
import com.example.ecommerceapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {


    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val allOrdersViewModel by viewModels<AllOrdersViewModel>()
    private val allOrdersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            setUpRecyclerView()

        lifecycleScope.launchWhenStarted {
            allOrdersViewModel.orders.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.INVISIBLE
                        Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading ->{
                        binding.progressbarAllOrders.visibility = View.VISIBLE

                    }
                    is Resource.Success ->{
                        binding.progressbarAllOrders.visibility = View.INVISIBLE
                        allOrdersAdapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
            adapter = allOrdersAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }


}