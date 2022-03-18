package com.example.foodorderingapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


//    val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
//    binding = fragmentBinding
//    return fragmentBinding.root

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomLogOut.setOnClickListener {
            buttonLogOut()
        }
    }


    fun buttonLogOut(){
        Firebase.auth.signOut()
        findNavController().navigate(R.id.action_profileFragment_to_signInActivity)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}