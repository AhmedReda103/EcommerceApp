package com.example.ecommerceapp.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.data.order.Order
import com.example.ecommerceapp.data.order.OrderStatus
import com.example.ecommerceapp.data.order.getOrderStatus
import com.example.ecommerceapp.databinding.BestDealsRvItemBinding
import com.example.ecommerceapp.databinding.OrderItemBinding

class AllOrdersAdapter  : RecyclerView.Adapter<AllOrdersAdapter.BestDealsViewHolder>() {

    inner class BestDealsViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
              tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val status = getOrderStatus(order.orderStatus)

                val colorDrawable = when(status){
                    OrderStatus.Canceled -> {
                        ColorDrawable(itemView.resources.getColor(R.color.g_red))
                    }
                    OrderStatus.Confirmed -> {
                        ColorDrawable(itemView.resources.getColor(R.color.green))

                    }
                    OrderStatus.Delivered -> {
                        ColorDrawable(itemView.resources.getColor(R.color.green))

                    }
                    OrderStatus.Ordered -> {
                        ColorDrawable(itemView.resources.getColor(R.color.g_orange_yellow))
                    }
                    OrderStatus.Returned -> {
                        ColorDrawable(itemView.resources.getColor(R.color.g_red))
                    }
                    OrderStatus.Shipped -> {
                        ColorDrawable(itemView.resources.getColor(R.color.green))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId ==newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this , diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent , false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    var onClick : ((Order) -> Unit)?=null


}