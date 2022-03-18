package com.example.foodorderingapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.databinding.FragmentHomeBinding
import com.example.foodorderingapp.data.model.Canteen
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by activityViewModels()

    private lateinit var mFirestore:FirebaseFirestore
    private lateinit var adapter: CanteenAdapter
    private lateinit var mQuery: Query
    private val  TAG = "test"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecylerview()
    }

     private fun initRecylerview() {
         mFirestore = FirebaseFirestore.getInstance()
         mQuery = mFirestore.collection("restaurants")
         val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Canteen>().setQuery(mQuery, Canteen::class.java).build()
         adapter = CanteenAdapter(recyclerViewOptions
         ) { canteenId, pos ->
             val action = HomeFragmentDirections.actionHomeFragmentToFoodsListFragment(canteenId)
             findNavController().navigate(action)
             viewModel.setCanteen(adapter.getItem(pos))
         }
         binding.canteenRvList.layoutManager = LinearLayoutManager(context)
         binding.canteenRvList.adapter = adapter

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
}

