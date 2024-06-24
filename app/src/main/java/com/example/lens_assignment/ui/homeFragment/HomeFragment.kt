package com.example.lens_assignment.ui.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lens_assignment.R
import com.example.lens_assignment.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binging:FragmentHomeBinding?=null
    private val binding get() = _binging!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binging= FragmentHomeBinding.inflate(layoutInflater,container,false)

        binding.switchFrag.setOnClickListener(){

            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }


        return binding.root

        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_home, container, false)

    }

}