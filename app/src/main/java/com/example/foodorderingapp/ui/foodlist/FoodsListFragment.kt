package com.example.foodorderingapp.ui.foodlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodorderingapp.FoodListAdapter


import com.example.foodorderingapp.databinding.FragmentFoodsListBinding
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.ui.home.OrderViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import android.content.Intent
import android.net.Uri
import android.widget.Toast





/**
 * [FlavorFragment] allows a user to choose a cupcake flavor for the order.
 */
class FoodsListFragment : Fragment() {
    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var mFirestore:FirebaseFirestore
    private lateinit var adapter: FoodListAdapter
    private lateinit var mQuery: Query
    private val  TAG = "test"
    private var _binding: FragmentFoodsListBinding? = null
    private val binding get() = _binding!!
    var foodList =  listOf<Food>()

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
        init()
    }

    private fun init(){
        mFirestore = FirebaseFirestore.getInstance()
        val canteenId = navigationArgs.canteenId
        val foodItems = mFirestore.collection("restaurants").document(canteenId).collection("FoodItems")


        if(viewModel.foodList.value == null) {
            val task = foodItems.get().addOnSuccessListener {
                foodList = it.toObjects()
                viewModel.initFoodList(foodList)
//                Log.d("TAG","${foodList.size}, and $foodList")
                //Log.d("TAG","inilizing f list ")
            }
        }

        adapter = FoodListAdapter(){ food, _ ->
            viewModel.setFood(food = food)
            val action = FoodsListFragmentDirections.actionFoodsListFragmentToFoodDetailsFragment()
            findNavController().navigate(action)
        }

        binding.foodRvList.adapter = adapter
        viewModel.foodList.observe(this.viewLifecycleOwner,{
            Log.d("TAG","fodlist observer called")
            adapter.submitList(it)
        })
        viewModel.count.observe(this,{
            Log.d("TAG","proceed to cart observer called")
            if(it>0){
                binding.proceedToCart.visibility = View.VISIBLE
            }
        })
        val canteen = viewModel.currentCanteen.value
        if(canteen!=null){
            binding.apply {
                textCanteenName.text = canteen.name
                tvOwnerName.text = canteen.ownerName
                imgCall.setOnClickListener {
                    try {
                        var str = canteen.mobileNo
                        var number = str.toLong()
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<$number>")
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "An error occurred $e", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
        binding.textCanteenName.text = viewModel.currentCanteen.value?.name ?: ""

        binding.proceedToCart.setOnClickListener {
            val action = FoodsListFragmentDirections.actionFoodsListFragmentToOrderFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


