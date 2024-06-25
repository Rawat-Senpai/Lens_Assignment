package com.example.lens_assignment.ui.homeFragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lens_assignment.R
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.FragmentAddTaskBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding:FragmentAddTaskBinding?= null
    private val binding get() = _binding!!

    private lateinit var placesClient: PlacesClient
    private var priority="'"
    private var selectedDateInMillis:Long=0L
    private var todayDate:Long=0L

    private val viewModel by viewModels<TaskViewModel>()

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
        setCurrentDate()
        bindObservers()
        bindViews()
        setupPrioritySpinner()

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAXzDd0QhaE7aF1l75w_zoE3mtjamQCmV0")
        }


    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        binding.dueDate.setText(currentDate)
        selectedDateInMillis = calendar.timeInMillis
        todayDate= calendar.timeInMillis
    }


    private fun bindViews() {
            binding.apply {

                dueDate.setOnClickListener(){
                    showDatePickerDialog()
                }


                submitButton.setOnClickListener(){

                    val title = taskTitle.text.toString()
                    val description = taskDescription.text.toString()
                    val dueDate = dueDate.text.toString()
                    val newTask = Task(title=title, description = description, dueDate = selectedDateInMillis, priorityLevel = priority , todayDate = todayDate )
                    viewModel.insertTask(newTask)

                    findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)

                }

                placesClient = Places.createClient(requireContext())

                taskLocation.setOnClickListener(){
                    openAutocompleteActivity()
                }


            }

    }


    private fun openAutocompleteActivity() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    binding.taskLocation.setText(place.name)
                    // Handle the selected place
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    // Handle the error
                }
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation
            }
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    private fun setupPrioritySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_levels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.priorityLevel.adapter = adapter

            binding.priorityLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected item from the spinner
                    val selectedPriority = parent?.getItemAtPosition(position).toString()
                    // Store the selected priority in a string variable
                    // You can save it to a class-level variable or any other appropriate location
                    // For example, you can save it to a ViewModel, a global variable, etc.
                    // Replace `selectedPriorityString` with your desired variable name
                    priority = selectedPriority
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected (optional)
                }
            }

        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"

                binding.dueDate.setText(selectedDate)

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                 selectedDateInMillis = selectedCalendar.timeInMillis
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }





    private fun bindObservers() {




    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}