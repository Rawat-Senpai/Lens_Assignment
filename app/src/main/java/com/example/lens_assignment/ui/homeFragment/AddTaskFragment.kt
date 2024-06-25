package com.example.lens_assignment.ui.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lens_assignment.R
import com.example.lens_assignment.databinding.FragmentAddTaskBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding:FragmentAddTaskBinding?= null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_task, container, false)

        _binding = FragmentAddTaskBinding.inflate(layoutInflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        bindViews()

    }

    private fun bindViews() {

    }

    private fun bindObservers() {

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}