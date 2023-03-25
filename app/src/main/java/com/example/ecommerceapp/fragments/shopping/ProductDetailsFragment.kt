package com.example.ecommerceapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.ColorsAdapter
import com.example.ecommerceapp.adapters.SizesAdapter
import com.example.ecommerceapp.adapters.ViewPager2Images
import com.example.ecommerceapp.data.CartProduct
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.hideBottomNavigationbar
import com.example.ecommerceapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()

    private lateinit var binding: FragmentProductDetailsBinding
    private val sizesAdapter by lazy {
        SizesAdapter()
    }
    private val colorsAdapter by lazy {
        ColorsAdapter()
    }
    private val viewPagerAdapter by lazy {
        ViewPager2Images()
    }
    private var selectedColor : Int ?=null
    private var selectedSize : String?=null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationbar()
        setupColorsRv()
        setupViewPager()
        setupSizesRv()

        binding.iconCloseImage.setOnClickListener {
            findNavController().navigateUp()
        }
        sizesAdapter.onItemClick={
            selectedSize = it
        }

        colorsAdapter.onItemClick={
            selectedColor = it
        }

        val product = args.product
        binding.apply {
            tvProductName.text = product.name
            tvProductDesc.text = product.description
            tvProductPrice.text = "$ ${product.price}"

            if(product.colors.isNullOrEmpty()){
                tvProductColors.visibility = View.INVISIBLE
            }
            if(product.sizes.isNullOrEmpty()){
                tvProductSizes.visibility = View.INVISIBLE
            }
        }
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

      binding.btnAddToCart.setOnClickListener {
          viewModel.addUpdateProductInCart(CartProduct(product , 1 , selectedColor , selectedSize))
      }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.btnAddToCart.stopAnimation()
                        Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {binding.btnAddToCart.startAnimation() }
                    is Resource.Success ->
                    {
                        binding.btnAddToCart.revertAnimation()
                        binding.btnAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext() , R.color.black))
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

    }



    private fun setupSizesRv() {
        binding.rvProductSizes.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sizesAdapter
        }
    }

    private fun setupViewPager() {
        binding.viewpagerProductImages.apply {
            adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvProductColors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorsAdapter
        }
    }




}
