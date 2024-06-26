package com.example.lens_assignment.ui.settingPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lens_assignment.R
import com.example.lens_assignment.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding:FragmentSettingBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_setting, container, false)
        _binding= FragmentSettingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialState()
        bindObservers()
    }

    private fun bindObservers() {



    }

    private fun setUpInitialState() {
        binding.apply {



        }
    }


}