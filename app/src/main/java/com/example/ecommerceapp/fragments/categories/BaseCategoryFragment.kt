package com.example.ecommerceapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.BestProductsAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerceapp.util.showBottomNavigationbar

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    lateinit var binding: FragmentBaseCategoryBinding

    protected val offerProductAdapter: BestProductsAdapter by lazy {
         BestProductsAdapter()
     }
    protected val  bestProductAdapter: BestProductsAdapter by lazy {
        BestProductsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvOffer()
        setupRvBestProduct()

        bestProductAdapter.onClick = {
            val product = Bundle().apply { putParcelable("product" , it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment , product)
        }

        offerProductAdapter.onClick = {
            val product = Bundle().apply { putParcelable("product" , it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment , product)
        }

        binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1) && dx !=0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(v.getChildAt(0).bottom <= v.height + scrollY){
                    onBestProductPagingRequest()
                }
            }

        })

    }

    open fun onBestProductPagingRequest() {
    }

    open fun onOfferPagingRequest() {
    }



    fun showOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.INVISIBLE
    }

    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.INVISIBLE
    }

    private fun setupRvBestProduct() {
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext() , 2 , GridLayoutManager.VERTICAL , false)
            adapter = bestProductAdapter
        }
    }

    private fun setupRvOffer() {
        binding.rvOfferProducts.apply {
            layoutManager = LinearLayoutManager(requireContext()  , LinearLayoutManager.HORIZONTAL , false)
            adapter = offerProductAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationbar()
    }
}