package com.example.foodorderingapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.data.model.Canteen
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.R


private val TAG ="test"

class CanteenAdapter(options: FirestoreRecyclerOptions<Canteen>, private  val onItemClicked:(String, Int) -> Unit): FirestoreRecyclerAdapter<Canteen,CanteenAdapter.CanteenViewHolder>(options){

    class CanteenViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val textview = itemView.findViewById<TextView>(R.id.tv_canteen_name)
        val tvDescription =  itemView.findViewById<TextView>(R.id.tv_canteen_description)
        val imageView:ImageView = itemView.findViewById(R.id.canteen_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenViewHolder {
        Log.d(TAG, "oncreate view holder alled")
        return CanteenViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_canteen, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CanteenViewHolder, position: Int, model: Canteen) {
        Log.d(TAG,"onbind view holder alled")
        holder.textview.text = model.name
        holder.tvDescription.text = model.category
        val canteenId = snapshots.getSnapshot(position).id
        Glide.with(holder.imageView).load(model.photo).into(holder.imageView)

        holder.itemView.setOnClickListener{
            onItemClicked(canteenId,position)
        }
    }

}

