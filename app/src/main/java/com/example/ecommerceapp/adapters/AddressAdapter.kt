package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Address
import com.example.ecommerceapp.databinding.AddressRvItemBinding
import com.example.ecommerceapp.databinding.SizeRvItemBinding

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: AddressRvItemBinding ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address, isSelected :Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if(isSelected){
                    buttonAddress.background = ContextCompat.getDrawable(itemView.context , R.color.g_blue)
                }else{
                    buttonAddress.background = ContextCompat.getDrawable(itemView.context , R.color.g_white)
                }
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var selectedPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address , selectedPosition==position)

        holder.binding.buttonAddress.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(address)
        }

    }

//    init {
//        differ.addListListener { previousList, currentList ->
//            notifyItemChanged(selectedPosition)
//        }
//    }

    var onItemClick: ((Address) -> Unit)? = null

}