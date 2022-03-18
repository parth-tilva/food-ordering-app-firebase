package com.example.foodorderingapp.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodorderingapp.adapter.IOrdersRVAdapter
//import com.example.foodorderingapp.adapter.IOrdersRVAdapter
import com.example.foodorderingapp.adapter.OrderAdapter
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.databinding.FragmentOrderBinding
import com.example.foodorderingapp.ui.home.OrderViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class OrderFragment : Fragment(), IOrdersRVAdapter {
    private val viewModel: OrderViewModel by activityViewModels()
    private var _binding : FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderAdapter
    private lateinit var mFirestore:FirebaseFirestore
    private lateinit var mQuery: Query
    private val  TAG = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){

        adapter = OrderAdapter(this)
        binding.orderRv.adapter = adapter
        viewModel.order.observe(this, {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.order.observe(this,{ foodList ->

            var total = 0
                    foodList.forEach { food ->
                    total += food.price * food.quantity
                }

            if(foodList.size>0){
                binding.btnPlaceOrder.apply {
                    visibility = View.VISIBLE
                    text = "Place Order(Total: Rs. $total)"
                    setOnClickListener {
                        val action = OrderFragmentDirections.actionOrderFragmentToPaymentFragment(amount = total)
                        findNavController().navigate(action)
                    }
                }
            }else{
                binding.btnPlaceOrder.visibility = View.INVISIBLE
            }
        })

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClickedPlus(food: Food) {
       viewModel.increaseQ(food)
    }

    override fun onMinusClicked(food: Food) {
       viewModel.decreaseQ(food)
    }

    override fun removeItem(food: Food) {
        viewModel.removeOrder(food)
    }
}



















/*  private fun initRecyclerView() {
         val uid = FirebaseAuth.getInstance().uid!!
        mFirestore = FirebaseFirestore.getInstance()
        mQuery = mFirestore.collection("users").document(uid).collection("Orders")
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Food>().setQuery(mQuery, Food::class.java).build()
        adapter = OrderAdapter(recyclerViewOptions,this)
        binding.orderRv.adapter = adapter

    }*/