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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.lens_assignment.R
import com.example.lens_assignment.adapters.TaskListAdapter
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.FragmentDashboardBinding
import com.example.lens_assignment.utils.AppInfo
import com.example.lens_assignment.utils.MyAppTheme
import com.example.lens_assignment.viewModelPackage.TaskViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : ThemeFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TaskViewModel>()
    private lateinit var taskAdapter: TaskListAdapter
    @Inject lateinit var appInfo:AppInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        bindViews()
        taskAdapter = TaskListAdapter(requireContext(), ::onTaskCompleted, ::onTaskLongHold)
        binding.rvPendingTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.refreshData()
        bindObserver()

    }

    override fun syncTheme(appTheme: AppTheme) {

        val myAppTheme = appTheme as MyAppTheme
        binding.apply {

            background.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))
            backgroundOne.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))
            textView.setTextColor(myAppTheme.mainTextColor(requireContext()))

            taskProgress.setTextColor(myAppTheme.mainTextColor(requireContext()))

            highPriorityLabel.setTextColor(myAppTheme.mainTextColor(requireContext()))
            highPriorityCount.setTextColor(myAppTheme.mainTextColor(requireContext()))

            mediumPriorityLabel.setTextColor(myAppTheme.mainTextColor(requireContext()))
            mediumPriorityCount.setTextColor(myAppTheme.mainTextColor(requireContext()))

            lowPriorityLabel.setTextColor(myAppTheme.mainTextColor(requireContext()))
            lowPriorityCount.setTextColor(myAppTheme.mainTextColor(requireContext()))

            dashboardHeading.setTextColor(myAppTheme.mainTextColor(requireContext()))

            textView.setTextColor(myAppTheme.mainTextColor(requireContext()))
//            userStatus.setTextColor(myAppTheme.mainTextColor(requireContext()))
        }

    }

    private fun bindObserver() {

        // observing all task priority count  from the view model about the
        viewModel.highPriorityCount.observe(viewLifecycleOwner) {
            updatePieChart()
            binding.highPriorityCount.text =  (viewModel.highPriorityCount.value ?: 0).toString()
        }
        viewModel.mediumPriorityCount.observe(viewLifecycleOwner) {
            updatePieChart()
            binding.mediumPriorityCount.text =  (viewModel.mediumPriorityCount.value ?: 0).toString()
        }
        viewModel.lowPriorityCount.observe(viewLifecycleOwner) {
            updatePieChart()
            binding.lowPriorityCount.text = (viewModel.lowPriorityCount.value ?: 0).toString()
        }
        // observing all completed and pending task  count  from the view model about the

        viewModel.completedTaskCount.observe(viewLifecycleOwner) { updateProgressBar() }
        viewModel.inCompletedTaskCount.observe(viewLifecycleOwner) { updateProgressBar() }

        viewModel.incompleteTasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                taskAdapter.submitList(it)
            }

        })


    }

    private fun updateProgressBar() {
        val completedTasks = viewModel.completedTaskCount.value ?: 0
        val incompleteTasks = viewModel.inCompletedTaskCount.value ?: 0
        val totalTasks = completedTasks + incompleteTasks

        Log.d(
            "chekcingTotalTask",
            "${totalTasks.toString()}  ${completedTasks.toString()}   ${incompleteTasks.toString()}"
        )

        if (totalTasks > 0) {
            val progress = (completedTasks.toFloat() / totalTasks) * 100
            binding.progressViewCompleted.progress = progress
            binding.progressViewCompleted.labelText = "Completed: $completedTasks / $totalTasks"

            val progressPending = (incompleteTasks.toFloat() / totalTasks) * 100
            binding.progressViewPending.progress = progressPending
            binding.progressViewPending.labelText = "Pending: $incompleteTasks / $totalTasks"
        } else {
            binding.progressViewPending.progress = (0f)
            binding.progressViewPending.labelText = "No tasks"
        }

    }

    private fun onTaskCompleted(task: Task) {
        Log.d("taskChecking", task.toString())
        val updateTask = task.copy(
            title = task.title,
            description = task.description,
            todayDate = task.todayDate,
            dueDate = task.dueDate,
            completed = !task.completed
        )
        viewModel.updateTask(updateTask)
        viewModel.refreshData()

    }

    private fun onTaskLongHold(task: Task) {

    }


    private fun updatePieChart() {
        val highPriority = viewModel.highPriorityCount.value ?: 0
        val mediumPriority = viewModel.mediumPriorityCount.value ?: 0
        val lowPriority = viewModel.lowPriorityCount.value ?: 0

        val entries = ArrayList<PieEntry>()
        if (highPriority > 0) entries.add(PieEntry(highPriority.toFloat(), "High"))
        if (mediumPriority > 0) entries.add(PieEntry(mediumPriority.toFloat(), "Medium"))
        if (lowPriority > 0) entries.add(PieEntry(lowPriority.toFloat(), "Low"))

        val dataSet = PieDataSet(entries, "Priority")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f


        Log.d("chckingData",appInfo.getDarkMode().toString())
        val textColor = if (appInfo.getDarkMode()) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.black)
        }

        val colors = arrayListOf(
            ContextCompat.getColor(requireContext(), R.color.high_priority),
            ContextCompat.getColor(requireContext(), R.color.medium_priority),
            ContextCompat.getColor(requireContext(), R.color.low_priority)
        )
        dataSet.setValueTextColor(textColor)

        dataSet.colors = colors


        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(14f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)


        binding.pieChart.data = data
        binding.pieChart.setEntryLabelColor(Color.WHITE)

        binding.pieChart.description.text = ""
        binding.pieChart.description.isEnabled = false
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}