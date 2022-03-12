package com.example.foodorderingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.FragmentFoodDetailsBinding
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject


class FoodDetailsFragment : Fragment() {
    private val viewModel:  OrderViewModel by activityViewModels()
    private lateinit var mFirestore: FirebaseFirestore
    private val  TAG = "test"
    private var _binding: FragmentFoodDetailsBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs : FoodDetailsFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            //  updateUi()
        viewModel.currentFood.value?.let { loadData(it)
        }
//        binding.btnAddToCart.setOnClickListener {
//            viewModel.addOrder()
//        }
    }

//    private fun updateUi() {
//        val canteenId = navigationArgs.canteenId
//        val foodId = navigationArgs.foodId
//        mFirestore = FirebaseFirestore.getInstance()
//        lateinit var foodObj : Food
//        val currentFood = mFirestore.collection("restaurants").document(canteenId).collection("FoodItems").document(foodId).get()
//
//        currentFood
//            .addOnSuccessListener { DocumentSS ->
//             // Log.d(TAG,"name is${DocumentSS.id}:${DocumentSS.data}")
//                foodObj = DocumentSS.toObject<Food>()!!
//                //Log.d(TAG,"food name          is            ${foodObj.name}")
//                loadData(foodObj)
//            }.addOnFailureListener {
//                Log.d(TAG,"logggggggggg ffffffaaaaaaaaaaaaaaaaaauuuuuuuuuillllllllllll")
//            }
//
//    }

    private fun loadData(food: Food) {
        binding.apply {
            if(food.isVeg){
                tvFoodType.text = "Food Type: Veg"
            }
            else{
                tvFoodType.text = "Food Type: Non veg"
            }
            tvFoodName.text = food.name
            tvPrice.text = "price: " + food.price.toString()

            Glide.with(ivFood).load(food.photo).into(ivFood)

            btnAddToCart.setOnClickListener {
                viewModel.addOrder()


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


//val data = hashMapOf("canteenId" to canteenId)
//
//        val firstlambda = fun (foodId: String , pos: Int){
//            try {
//
//                foodItems.document(foodId)
//                    .set(data, SetOptions.merge())
//                    .addOnSuccessListener {
//                    Log.d(TAG,"set referece success")
//                }





//currentFood.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // Document found in the offline cache
//                val document = task.result
//                Log.d(TAG, " document data: ${document?.data}")
//            } else {
//                Log.d(TAG, " get failed: ", task.exception)
//            }
//        }