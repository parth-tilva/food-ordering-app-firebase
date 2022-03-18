package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.databinding.ItemOrderBinding

private val TAG ="OrderAdapter"



class OrderAdapter(private val listener: IOrdersRVAdapter): ListAdapter<Food, OrderAdapter.OrderViewHolder>(DiffCallback) {

    class OrderViewHolder(private var binding: ItemOrderBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(food: Food){
                binding.tvFoodName.text = food.name
                binding.tvPrice.text = food.price.toString()
                Glide.with(binding.foodImage).load(food.photo).into(binding.foodImage)
                binding.quntity.text = food.quantity.toString()
                binding.executePendingBindings()
            }

    }
    companion object DiffCallback : DiffUtil.ItemCallback<Food>(){
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding= ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val viewholder =  OrderViewHolder(binding)
        binding.decrement.setOnClickListener {
            val pos = viewholder.adapterPosition
            val food = getItem(pos)
            if(food.quantity<=1){
                listener.removeItem(food)
            }else{
                listener.onMinusClicked(food)
            }
        }
        binding.increment.setOnClickListener {
            val pos = viewholder.adapterPosition
            val food = getItem(pos)
            listener.onItemClickedPlus(food)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item  = getItem(position)
        holder.bind(item)
    }
}

interface IOrdersRVAdapter{
    fun onItemClickedPlus(food: Food)
    fun onMinusClicked(food: Food)
    fun removeItem(food: Food)
}


//class OrderAdapter(options: FirestoreRecyclerOptions<Food>, private val listener: IOrdersRVAdapter): FirestoreRecyclerAdapter<Food, OrderAdapter.OrderViewHolder>(options){
//
//    class OrderViewHolder(private val binding: ItemOrderBinding):RecyclerView.ViewHolder(binding.root) {
//        fun bind(food: Food){
//                binding.tvFoodName.text = food.name
//                binding.tvPrice.text = "Price: " +food.price.toString()
//                Glide.with(binding.foodImage).load(food.photo).into(binding.foodImage)
//                binding.quntity.text = food.quantity.toString()
//            }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
//        val binding= ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        val viewholder =  OrderViewHolder(binding)
//        binding.decrement.setOnClickListener {
//            val pos = viewholder.adapterPosition
//            val food = getItem(pos)
//            listener.onMinusClicked(food)
//            if(food.quantity>1){
//                //notifyItemChanged(pos)
//            }
//            else{
//                //notifyItemRemoved(pos)
//            }
//        }
//        binding.increment.setOnClickListener {
//            val pos = viewholder.adapterPosition
//            val food = getItem(pos)
//            listener.onItemClickedPlus(food)
//            //notifyItemChanged(pos)
//        }
//        return viewholder
//    }
//
//    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: Food) {
//        val item  = getItem(position)
//        holder.bind(item)
//    }
//}

//interface IOrdersRVAdapter{
//    fun onItemClickedPlus(food: Food)
//    fun onMinusClicked(food: Food)
//}