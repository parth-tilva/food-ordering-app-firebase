package com.example.foodorderingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.CanteenAdapter
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
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeFragment = this
        initRecylerview()
    }

     private fun initRecylerview() {
         mFirestore = FirebaseFirestore.getInstance()
         mQuery = mFirestore.collection("restaurants")
         val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Canteen>().setQuery(mQuery, Canteen::class.java).build()
         val lamda = fun(canteenId: String ,pos: Int){
             val action = HomeFragmentDirections.actionHomeFragmentToFoodsListFragment(canteenId)
             findNavController().navigate(action)
             viewModel.setCanteen(adapter.getItem(pos))
         }
         adapter = CanteenAdapter(recyclerViewOptions,lamda)
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


// }
//        catch (e:FirebaseException){
//            Log.d(TAG,"exception is e:${e}")
//        }


//    fun ontest(){
//        val db = Firebase.firestore
//        Log.d(TAG,"ontest method called")
//
//        val testuserdoc = hashMapOf(
//            "First_name" to "kano",
//            "Last_name" to "patel",
//            "born" to 1999
//        )
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
//   mQuery.get()
////             .addOnSuccessListener {
////                 for(name in it){
////                     Log.d(TAG,"name is${name.id}:${name.data}")
////                 }
////             }
////             .addOnFailureListener{
////                 Log.d(TAG,"failto get data from firebase : $it")
////             }