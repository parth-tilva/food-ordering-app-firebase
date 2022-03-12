package com.example.foodorderingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.foodorderingapp.OrderFragment
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.databinding.ItemOrderBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_order.view.*


private val TAG ="OrderAdapter"



//class OrderAdapter(  private val listener: IOrdersRVAdapter): ListAdapter<Food, OrderAdapter.OrderViewHolder>(DiffCallback) {
//
//    class OrderViewHolder(private var binding: ItemOrderBinding)
//        :RecyclerView.ViewHolder(binding.root){
//            fun bind(food: Food){
//                binding.tvFoodName.text = food.name
//                binding.tvPrice.text = food.price.toString()
//                Glide.with(binding.foodImage).load(food.photo).into(binding.foodImage)
//                binding.executePendingBindings()
//                binding.quntity.text = food.quantity.toString()
//            }
//
//    }
//    companion object DiffCallback : DiffUtil.ItemCallback<Food>(){
//        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
//            return oldItem.name == newItem.name
//        }
//
//        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
//            return oldItem.photo == oldItem.photo
//        }
//
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
//        val binding= ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//
//        val viewholder =  OrderViewHolder(binding)
//        binding.decrement.setOnClickListener {
//            val pos = viewholder.adapterPosition
//            val food = getItem(pos)
//            listener.OnMinusClicked(food)
//            if(food.quantity>1){
//                notifyItemChanged(pos)
//            }
//            else{
//                notifyItemRemoved(pos)
//            }
//        }
//        binding.increment.setOnClickListener {
//            val pos = viewholder.adapterPosition
//            val food = getItem(pos)
//            listener.onItemClickedPluse(food)
//            notifyItemChanged(pos)
//        }
//        return viewholder
//    }
//
//    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
//        val item  = getItem(position)
//        holder.bind(item)
//
//    }
//}
//
//interface IOrdersRVAdapter{
//    fun onItemClickedPluse(food: Food)
//    fun OnMinusClicked(food: Food)
//}


class OrderAdapter(options: FirestoreRecyclerOptions<Food>, private val listener: IOrdersRVAdapter): FirestoreRecyclerAdapter<Food, OrderAdapter.OrderViewHolder>(options){

    class OrderViewHolder(private val binding: ItemOrderBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food){
                binding.tvFoodName.text = food.name
                binding.tvPrice.text = "Price: " +food.price.toString()
                Glide.with(binding.foodImage).load(food.photo).into(binding.foodImage)
                binding.quntity.text = food.quantity.toString()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding= ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewholder =  OrderViewHolder(binding)
        binding.decrement.setOnClickListener {
            val pos = viewholder.adapterPosition
            val food = getItem(pos)
            listener.OnMinusClicked(food)
            if(food.quantity>1){
                //notifyItemChanged(pos)
            }
            else{
                //notifyItemRemoved(pos)
            }
        }
        binding.increment.setOnClickListener {
            val pos = viewholder.adapterPosition
            val food = getItem(pos)
            listener.onItemClickedPluse(food)
            //notifyItemChanged(pos)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: Food) {
        val item  = getItem(position)
        holder.bind(item)
    }
}


interface IOrdersRVAdapter{
    fun onItemClickedPluse(food: Food)
    fun OnMinusClicked(food: Food)
}