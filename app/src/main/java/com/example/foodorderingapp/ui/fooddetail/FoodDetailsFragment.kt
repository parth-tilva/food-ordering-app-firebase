package com.example.foodorderingapp.ui.fooddetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.FragmentFoodDetailsBinding
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.ui.home.OrderViewModel
import com.google.firebase.firestore.FirebaseFirestore


class FoodDetailsFragment : Fragment() {
    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var mFirestore: FirebaseFirestore
    private val  TAG = "test"
    private var _binding: FragmentFoodDetailsBinding? = null
    private val binding get() = _binding!!
    //private val navigationArgs : FoodDetailsFragmentArgs by navArgs()


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
        viewModel.currentFood.value?.let {
            loadData(it)
        }
    }


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

            if(food.isOrdered){
                btnAddToCart.text = "remove"
            }else{
                btnAddToCart.text = "add"
            }

            btnAddToCart.setOnClickListener {
                if(food.isOrdered){
                    viewModel.removeOrder()
                    btnAddToCart.text = "add"
                }else{
                    viewModel.addOrder()
                    btnAddToCart.text = "remove"
                }
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
