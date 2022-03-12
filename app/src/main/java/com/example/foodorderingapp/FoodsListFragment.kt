package com.example.foodorderingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager


import com.example.foodorderingapp.databinding.FragmentFoodsListBinding
import com.example.foodorderingapp.data.model.Food
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_foods_list.*

/**
 * [FlavorFragment] allows a user to choose a cupcake flavor for the order.
 */
class FoodsListFragment : Fragment() {
    private val viewModel:  OrderViewModel by activityViewModels()
    private lateinit var mFirestore:FirebaseFirestore
    private lateinit var adapter: FoodListAdapter
    private lateinit var mQuery: Query
    private val  TAG = "test"
    private var _binding: FragmentFoodsListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs : FoodsListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mFirestore = FirebaseFirestore.getInstance()
        val canteenId = navigationArgs.canteenId
        val foodItems = mFirestore.collection("restaurants").document(canteenId).collection("FoodItems")
        mQuery = foodItems // mFirestore.collection("restaurants").document(canteenId).collection("FoodItems")
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Food>().setQuery(mQuery, Food::class.java).build()
        binding.foodRvList.layoutManager = LinearLayoutManager(context)

        val data = hashMapOf("canteenId" to canteenId)

        val firstlambda = fun (foodId: String , pos: Int){
            try {

                foodItems.document(foodId)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener {
                    Log.d(TAG,"set referece success")
                }
                    .addOnFailureListener {
                        Log.d(TAG,"set referece fauilder")
                    }
            }catch (e:Exception){
                Log.d(TAG,"exception on set $e")
            }

            val action = FoodsListFragmentDirections.actionFoodsListFragmentToFoodDetailsFragment(foodId = foodId, canteenId = canteenId )
            findNavController().navigate(action)
            viewModel.setFood(adapter.snapshots[pos])
        }
        adapter = FoodListAdapter(recyclerOptions,firstlambda)
        binding.foodRvList.adapter = adapter
        binding.apply {
            viewModel.currentCanteen.value?.let{
                textCanteenName.text  = it.name
            }
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}