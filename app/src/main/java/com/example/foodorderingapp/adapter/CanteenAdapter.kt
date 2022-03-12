package com.example.foodorderingapp.adapter

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
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.foodorderingapp.HomeFragmentDirections
import com.example.foodorderingapp.R


private val TAG ="test"

class CanteenAdapter(options: FirestoreRecyclerOptions<Canteen> ,private  val onItemClicked:(String, Int) -> Unit): FirestoreRecyclerAdapter<Canteen,CanteenAdapter.CanteenViewHolder>(options){

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
//
//            val action = HomeFragmentDirections.actionHomeFragmentToFoodsListFragment(canteenId)
//            holder.itemView.findNavController().navigate(action)


//            val db = Firebase.firestore

//            val food = reandomFoods().getrandom()
//            try{
//                val canteen=db.collection("restaurants").document(id).collection("what")
//                    .add(food).addOnSuccessListener {
//                    Log.d(TAG,"uploaded")
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d(TAG, "get failed with ", exception)
//                    }
//
//            }catch(e:Exception){
//                Log.d(TAG,"$e")
//            }




//                    canteen.collection("testaddOnclikItems").add(Food("Punjabi",60,true,"2 roti, Subji, Dal Chawal"))

//    fun ontest(){
//        val db = Firebase.firestore
//        Log.d(TAG,"ontest method called")
//

//        db.collection("test_collection")
//            .add(testuserdoc)
//            .addOnSuccessListener {
//                    docref-> binding.textView.text = docref.id
//                Log.d(TAG,"onsucces llistner")
//            }
//            .addOnFailureListener {
//                    e -> binding.textView.text = e.toString()
//                Log.d(TAG,"onfalier llistner")
//
//            }
//
//    }
