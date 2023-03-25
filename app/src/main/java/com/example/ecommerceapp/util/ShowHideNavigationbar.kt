package com.example.ecommerceapp.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.example.ecommerceapp.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.showBottomNavigationbar(){
    val bottomNavigationbar = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    bottomNavigationbar.visibility= View.VISIBLE
}

fun Fragment.hideBottomNavigationbar(){
    val bottomNavigationbar = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    bottomNavigationbar.visibility= View.GONE
}