package com.example.lens_assignment.ui.homeFragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.lens_assignment.R
import com.example.lens_assignment.adapters.TaskListAdapter
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.DeletePopupBinding
import com.example.lens_assignment.databinding.FragmentHomeBinding
import com.example.lens_assignment.utils.SwipeToDeleteCallback
import com.example.lens_assignment.utils.TaskActionListener
import com.example.lens_assignment.viewModelPackage.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class HomeFragment : ThemeFragment(), TaskActionListener {

    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TaskViewModel>()
    lateinit var  taskAdapter:TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        taskAdapter = TaskListAdapter(requireContext(), ::onTaskCompleted ,::onTaskLongHold)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        bindViews()

    }

    override fun syncTheme(appTheme: AppTheme) {

    }

    private fun bindViews() {

        binding.apply {

            addTask.setOnClickListener(){
                findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
            }


            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            taskRecyclerView.layoutManager = layoutManager
            taskRecyclerView.setHasFixedSize(false)

            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(taskAdapter,requireContext(),this@HomeFragment))
            itemTouchHelper.attachToRecyclerView(taskRecyclerView)
            taskRecyclerView.adapter = taskAdapter



            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // This method is called before the text in the EditText is changed
                }

                override fun onTextChanged(
                    query: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    performSearch()


                }

                override fun afterTextChanged(s: Editable?) {
                    val newText = s.toString()

                }
            }

            searchEdt.addTextChangedListener(textWatcher)

            searchEdt.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    event != null && event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    hideKeyboard(requireActivity())
                    // Perform your search function here
//                    performSearch()
                    return@setOnEditorActionListener true
                }
                false
            }




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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.taskEvents.collect { event->

                if ( event is TaskViewModel.TaskEvents.ShowUndoSnackBar){
                    Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                        .setAction("Undo"){
                            viewModel.insertTask(event.task)
                        }.show()
                }


            }

        }


    }


    private fun onTaskCompleted(task:Task){
        Log.d("taskChecking",task.toString())

        val updateTask = task.copy(title = task.title, description = task.description, todayDate = task.todayDate, dueDate = task.dueDate, completed = !task.completed)
        viewModel.updateTask(updateTask)

    }

    private fun onTaskLongHold(task: Task){
        val bundle = Bundle()
        bundle.putSerializable("task", Gson().toJson(task))
        findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment,bundle)
        Log.d("task response",task.toString())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

    override fun onEditTask(task: Task) {

        val bundle = Bundle()
        bundle.putSerializable("task", Gson().toJson(task))
        findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment,bundle)
        Log.d("task response",task.toString())
    }

    override fun onDeleteTask(task: Task) {

        Log.d("task response",task.toString())

        val view =
            LayoutInflater.from(context).inflate(R.layout.delete_popup, null)
        val builder =
            AlertDialog.Builder(requireContext(), R.style.dialog_transparent_style)
                .setView(view)

        val dialogBinding = DeletePopupBinding.bind(view)
        val mAlertDialog = builder.show()

        val window = mAlertDialog.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params

        window!!.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(R.color.transparent)


        dialogBinding.title.text =
            "Are you sure you want to delete this task?"

        dialogBinding.deleteBtn.setOnClickListener {
            viewModel.deleteTask(task)
            mAlertDialog.dismiss()
        }
        dialogBinding.cancleBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }

        builder.setCancelable(true)



    }

    private fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    private fun performSearch() {
        // Replace this with your search functionality
        val query = binding.searchEdt.text.toString()
        val searchQuery = "%$query%"

        viewModel.searchDatabase(searchQuery)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
                list.let {
                    taskAdapter.submitList(list)
                }

            }
        }

    }

}