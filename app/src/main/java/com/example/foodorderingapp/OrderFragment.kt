package com.example.foodorderingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.CanteenAdapter
import com.example.foodorderingapp.adapter.IOrdersRVAdapter
//import com.example.foodorderingapp.adapter.IOrdersRVAdapter
import com.example.foodorderingapp.adapter.OrderAdapter
import com.example.foodorderingapp.data.model.Canteen
import com.example.foodorderingapp.data.model.Food
import com.example.foodorderingapp.data.model.User
import com.example.foodorderingapp.databinding.FragmentOrderBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject


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
        initRecyclerView()
    }

    private fun initRecyclerView() {
         val uid = FirebaseAuth.getInstance().uid!!
        mFirestore = FirebaseFirestore.getInstance()
        mQuery = mFirestore.collection("users").document(uid).collection("Orders")
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Food>().setQuery(mQuery, Food::class.java).build()
        adapter = OrderAdapter(recyclerViewOptions,this)
        binding.orderRv.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRv.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClickedPluse(food: Food) {
        viewModel.increaseQ(food)
    }

    override fun OnMinusClicked(food: Food) {
        viewModel.decreaseQ(food)
    }



//        val orderAdapter = OrderAdapter(this)
//        binding.orderRv.adapter = orderAdapter
//        orderAdapter.submitList(viewModel.order.value)






//    fun bindRecyclerView(recyclerView: RecyclerView,
//                    data: List<MarsPhoto>?) {
//   val adapter = recyclerView.adapter as PhotoGridAdapter
//   adapter.submitList(data)
//
//}

//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}