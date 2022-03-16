package com.example.foodorderingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.databinding.FoodItemBinding
import com.example.foodorderingapp.databinding.FragmentFoodsListBinding
import com.example.foodorderingapp.databinding.ItemOrderBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

private val  TAG = "test"


class FoodListAdapter( private val onItemClicked: (Food,Int) -> Unit):ListAdapter<Food,FoodListAdapter.FoodListViewHolder>(DiffCallBack) {

    class FoodListViewHolder(val binding: FoodItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(food: Food){
            binding.apply {
                tvCanteenName.text = food.name
                tvCanteenDescription.text = food.foodDes
                if(food.isOrdered){
                    imgIsOrdered.visibility = View.VISIBLE
                }else{
                    imgIsOrdered.visibility = View.INVISIBLE
                }
            }
            Glide.with(binding.canteenImage).load(food.photo).into(binding.canteenImage)
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Food>(){

        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(
            FoodItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClicked(item,position)
        }
    }



}


interface IOrdersRVAdapter{
    fun onItemClickedPluse(food: Food)
    fun OnMinusClicked(food: Food)
}






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

//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
//        val binding= ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//
//        val viewholder =  OrderViewHolder(binding)
//        binding.decrement.setOnClickListener {
//            val pos = viewholder.adapterPosition
//            val food = getItem(pos)
//            listener.onMinusClicked(food)
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
//            listener.onItemClickedPlus(food)
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
//    fun onItemClickedPlus(food: Food)
//    fun onMinusClicked(food: Food)
//}















/*
* class FoodListViewHolder(iView: View):RecyclerView.ViewHolder(iView) {

        val textview = iView.findViewById<TextView>(R.id.tv_canteen_name)
        val tvDescription =  iView.findViewById<TextView>(R.id.tv_canteen_description)
        val imageView: ImageView = iView.findViewById(R.id.canteen_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_canteen, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int, model: Food) {
        holder.textview.text = model.name
        val foodId = snapshots.getSnapshot(position).id
        holder.tvDescription.text = model.foodDes
        Glide.with(holder.imageView).load(model.photo).into(holder.imageView)
        holder.itemView.setOnClickListener {
            onItemClicked(foodId,position)
        }
    }*/