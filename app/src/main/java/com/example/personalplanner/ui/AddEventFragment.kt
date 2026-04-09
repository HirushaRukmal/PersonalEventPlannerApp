package com.example.personalplanner.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.personalplanner.R
import com.example.personalplanner.data.Event
import com.example.personalplanner.data.EventViewModel
import com.example.personalplanner.databinding.FragmentAddEventBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()

    private val categories = arrayOf("Work", "Social", "Travel")
    private var selectedDateTimeMillis: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEventBinding.bind(view)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        binding.buttonPickDateTime.setOnClickListener {
            pickDateTime()
        }

        binding.buttonSaveEvent.setOnClickListener {
            saveEvent()
        }
    }

    private fun pickDateTime() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, hourOfDay, minute, 0)
                        }
                        selectedDateTimeMillis = selectedCalendar.timeInMillis

                        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                        binding.textViewSelectedDateTime.text =
                            formatter.format(selectedCalendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveEvent() {
        val title = binding.editTextTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val location = binding.editTextLocation.text.toString().trim()
        val dateMillis = selectedDateTimeMillis

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (dateMillis == null) {
            Toast.makeText(requireContext(), "Date must be selected", Toast.LENGTH_SHORT).show()
            return
        }

        if (dateMillis < System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "Past dates are not allowed", Toast.LENGTH_SHORT).show()
            return
        }

        val event = Event(
            title = title,
            category = category,
            location = location,
            dateTimeMillis = dateMillis
        )

        viewModel.insert(event)
        Toast.makeText(requireContext(), "Event saved successfully", Toast.LENGTH_SHORT).show()

        binding.editTextTitle.text?.clear()
        binding.editTextLocation.text?.clear()
        binding.textViewSelectedDateTime.text = "No date selected"
        selectedDateTimeMillis = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}