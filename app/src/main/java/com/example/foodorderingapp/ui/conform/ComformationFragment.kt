package com.example.foodorderingapp.ui.conform

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodorderingapp.databinding.FragmentComformationBinding
import com.example.foodorderingapp.ui.home.OrderViewModel


class ComformationFragment : Fragment() {
    lateinit var binding:FragmentComformationBinding
    private val viewModel: OrderViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComformationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            viewModel.orderReset()
            val action = ComformationFragmentDirections.actionComformationFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}