package com.example.foodorderingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.data.model.Food
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

private val  TAG = "test"


class FoodListAdapter(options: FirestoreRecyclerOptions<Food>, private val onItemClicked: (String,Int) -> Unit):FirestoreRecyclerAdapter<Food,FoodListAdapter.FoodListViewHolder>(options) {

    class FoodListViewHolder(iView: View):RecyclerView.ViewHolder(iView) {

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
    }
}