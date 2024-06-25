package com.example.lens_assignment.ui.homeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lens_assignment.R
import com.example.lens_assignment.adapters.TaskListAdapter
import com.example.lens_assignment.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TaskViewModel>()
    private val taskAdapter:TaskListAdapter by lazy { TaskListAdapter () }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        bindViews()

    }

    private fun bindViews() {

        binding.apply {

            addTask.setOnClickListener(){
                findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
            }


            taskRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            taskRecyclerView.setHasFixedSize(false)
            taskRecyclerView.adapter=taskAdapter


        }

    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.task.collect{
                Log.d("NotesSize",it.size.toString()+" + "+it.toString())
                it.let {
                    taskAdapter.submitList(it)
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

}