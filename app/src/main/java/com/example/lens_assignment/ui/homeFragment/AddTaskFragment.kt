package com.example.lens_assignment.ui.homeFragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.lens_assignment.R
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.FragmentAddTaskBinding
import com.example.lens_assignment.utils.MyAppTheme
import com.example.lens_assignment.viewModelPackage.TaskViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.PriorityQueue


@AndroidEntryPoint
class AddTaskFragment : ThemeFragment(), OnMapReadyCallback {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var placesClient: PlacesClient
    private var priority = "'"
    private var selectedDateInMillis: Long = 0L
    private var todayDate: Long = 0L

    private val viewModel by viewModels<TaskViewModel>()
    private var task: Task? = null

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAXzDd0QhaE7aF1l75w_zoE3mtjamQCmV0")
        }

        setInitialState()


    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme

        binding.apply {
            background.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))

            taskTxt.setTextColor(myAppTheme.mainTextColor(requireContext()))
            taskDescriptionTxt.setTextColor(myAppTheme.mainTextColor(requireContext()))

            dueDateTxt.setTextColor(myAppTheme.mainTextColor(requireContext()))
            priorityTxt.setTextColor(myAppTheme.mainTextColor(requireContext()))

            taskLocation.setTextColor(myAppTheme.mainTextColor(requireContext()))

            taskTitle.setTextColor(myAppTheme.mainEditTextColor(requireContext()))
            taskTitle.setHintTextColor(myAppTheme.changeTextHintColor(requireContext()))

            taskDescription.setTextColor(myAppTheme.mainEditTextColor(requireContext()))
            taskDescription.setHintTextColor(myAppTheme.changeTextHintColor(requireContext()))


            dueDate.setTextColor(myAppTheme.mainEditTextColor(requireContext()))

            locationTxt.setTextColor(myAppTheme.mainTextColor(requireContext()))

            taskLocation.setTextColor(myAppTheme.mainEditTextColor(requireContext()))
            taskLocation.setHintTextColor(myAppTheme.changeTextHintColor(requireContext()))
        }


    }

    private fun setInitialState() {
        setupPrioritySpinner()

        val jsonTask = arguments?.getString("task")
        if (jsonTask != null) {

            task = Gson().fromJson(jsonTask, Task::class.java)

            task?.let {
                binding.apply {
                    layoutCompleted.isVisible=true
                    taskTitle.setText(it.title)
                    taskDescription.setText(it.description)
                    taskLocation.setText(it.location)
                    latitude = it.latitude
                    longitude = it.longitude

                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    dueDate.text = formatter.format(it.dueDate)
                    setPriority(it.priorityLevel!!)
                }
            }

            binding.apply {

                dueDate.setOnClickListener() {
                    showDatePickerDialog()
                }


                submitButton.setOnClickListener() {
                Log.d("checkingCheckedVal",completedCheckbox.isChecked.toString())
                    val title = taskTitle.text.toString()
                    val description = taskDescription.text.toString()
                    val location = taskLocation.text.toString()
                    val newTask = task!!.copy(
                        title = title,
                        description = description,
                        dueDate = selectedDateInMillis,
                        priorityLevel = priority,
                        location = location,
                        longitude = longitude,
                        latitude = latitude,
                        completed = completedCheckbox.isChecked
                    )
                    viewModel.updateTask(newTask)

                    findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)

                }

                placesClient = Places.createClient(requireContext())

                taskLocation.setOnClickListener() {
                    openAutocompleteActivity()
                }

            }

        } else {
            setCurrentDate()

            binding.apply {
                layoutCompleted.isVisible=false
                dueDate.setOnClickListener() {
                    showDatePickerDialog()
                }


                submitButton.setOnClickListener() {

                    val title = taskTitle.text.toString()
                    val description = taskDescription.text.toString()
                    val dueDate = dueDate.text.toString()
                    val location = taskLocation.text.toString()
                    val newTask = Task(
                        title = title,
                        description = description,
                        dueDate = selectedDateInMillis,
                        priorityLevel = priority,
                        todayDate = todayDate,
                        location = location,
                        longitude = longitude,
                        latitude = latitude,
                        completed = false
                    )
                    viewModel.insertTask(newTask)

                    findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)
                }
                placesClient = Places.createClient(requireContext())
                taskLocation.setOnClickListener() {
                    openAutocompleteActivity()
                }
            }
        }

    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        binding.dueDate.setText(currentDate)
        selectedDateInMillis = calendar.timeInMillis
        todayDate = calendar.timeInMillis
    }


    private fun setPriority(priority: String) {
        val adapter = binding.priorityLevel.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(priority.capitalize())
        binding.priorityLevel.setSelection(position)
    }


    private fun openAutocompleteActivity() {
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
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
                    place.latLng?.let { it1 -> Log.d("latLong", it1.toString()) }
                    Log.d(
                        "checkingLatLong",
                        place.latLng?.latitude.toString() + "  " + place.latLng?.longitude
                    )
                    place.latLng?.let { latLng ->
                        Log.d("checkingLatLong", "${latLng.latitude}, ${latLng.longitude}")
                        this.latLng = latLng
                        googleMap?.let { addMarker(latLng) }
                        latitude = latLng.latitude
                        longitude= latLng.longitude


                    }
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

            binding.priorityLevel.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedPriority = parent?.getItemAtPosition(position).toString()

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
                val selectedDate = "${selectedDay.toString().padStart(2, '0')}/${
                    (selectedMonth + 1).toString().padStart(2, '0')
                }/$selectedYear"

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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true

        val jsonTask = arguments?.getString("task")
        if (jsonTask != null) {

            task.let {
                addMarker(LatLng(latitude,longitude))
            }

        } else{
            latLng?.let { addMarker(it) }
        }


    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng).title("Selected Location")
        googleMap?.addMarker(markerOptions)

        // Move camera to the marker
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

}