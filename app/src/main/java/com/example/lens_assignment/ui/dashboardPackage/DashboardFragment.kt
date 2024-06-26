package com.example.lens_assignment.ui.dashboardPackage

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lens_assignment.R
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.FragmentDashboardBinding
import com.example.lens_assignment.databinding.FragmentEditTaskDetailsBinding
import com.example.lens_assignment.ui.homeFragment.TaskViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding:FragmentDashboardBinding?= null
    private val binding get()= _binding!!
    private val viewModel by viewModels<TaskViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding= FragmentDashboardBinding.inflate(layoutInflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        bindViews()
        bindObserver()

    }

    private fun bindObserver() {

        // observing all task priority count  from the view model about the

        viewModel.highPriorityCount.observe(viewLifecycleOwner) { updatePieChart() }
        viewModel.mediumPriorityCount.observe(viewLifecycleOwner) { updatePieChart() }
        viewModel.lowPriorityCount.observe(viewLifecycleOwner) { updatePieChart() }

        // observing all completed and pending task  count  from the view model about the

        viewModel.completedTaskCount.observe(viewLifecycleOwner) { updateProgressBar() }
        viewModel.inCompletedTaskCount.observe(viewLifecycleOwner) { updateProgressBar() }



    }
    private fun updateProgressBar() {
        val completedTasks = viewModel.completedTaskCount.value ?: 0
        val incompleteTasks = viewModel.inCompletedTaskCount.value ?: 0
        val totalTasks = completedTasks + incompleteTasks

        Log.d("chekcingTotalTask","${totalTasks.toString()}  ${completedTasks.toString()}   ${incompleteTasks.toString()}" )

        if (totalTasks > 0) {
            val progress = (completedTasks.toFloat() / totalTasks) * 100
            binding.progressView.progress= progress
            binding.progressView.labelText = "Completed: $completedTasks / $totalTasks"
//            binding.progressView.labelText = "${progress.toInt()}%"
        } else {
            binding.progressView.progress=(0f)
            binding.progressView.labelText = "No tasks"
//            binding.progressView.progressText = "0%"
        }
    }

    private fun updatePieChart() {
        val highPriority = viewModel.highPriorityCount.value ?: 0
        val mediumPriority = viewModel.mediumPriorityCount.value ?: 0
        val lowPriority = viewModel.lowPriorityCount.value ?: 0

        val entries = ArrayList<PieEntry>()
        if (highPriority > 0) entries.add(PieEntry(highPriority.toFloat(), "High"))
        if (mediumPriority > 0) entries.add(PieEntry(mediumPriority.toFloat(), "Medium"))
        if (lowPriority > 0) entries.add(PieEntry(lowPriority.toFloat(), "Low"))

        val dataSet = PieDataSet(entries, "Task Priority")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = arrayListOf(
            ContextCompat.getColor(requireContext(), R.color.high_priority),
            ContextCompat.getColor(requireContext(), R.color.medium_priority),
            ContextCompat.getColor(requireContext(), R.color.low_priority)
        )
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.pieChart.data = data

        binding.pieChart.description.text=""
        binding.pieChart.description.isEnabled=false
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}