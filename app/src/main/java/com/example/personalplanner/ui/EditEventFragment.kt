package com.example.personalplanner.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalplanner.R
import com.example.personalplanner.data.Event
import com.example.personalplanner.data.EventViewModel
import com.example.personalplanner.databinding.FragmentEditEventBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditEventFragment : Fragment(R.layout.fragment_edit_event) {

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()

    private val categories = arrayOf("Work", "Social", "Travel")
    private var currentEvent: Event? = null
    private var selectedDateTimeMillis: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditEventBinding.bind(view)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        val eventId = arguments?.getInt("eventId") ?: return

        lifecycleScope.launch {
            val event = viewModel.getEventById(eventId)
            if (event != null) {
                currentEvent = event
                selectedDateTimeMillis = event.dateTimeMillis
                populateFields(event)
            }
        }

        binding.buttonPickDateTime.setOnClickListener {
            pickDateTime()
        }

        binding.buttonUpdateEvent.setOnClickListener {
            updateEvent()
        }

        binding.buttonDeleteEvent.setOnClickListener {
            deleteEvent()
        }
    }

    private fun populateFields(event: Event) {
        binding.editTextTitle.setText(event.title)
        binding.editTextLocation.setText(event.location)

        val categoryIndex = categories.indexOf(event.category)
        if (categoryIndex >= 0) {
            binding.spinnerCategory.setSelection(categoryIndex)
        }

        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        binding.textViewSelectedDateTime.text = formatter.format(Date(event.dateTimeMillis))
    }

    private fun pickDateTime() {
        val calendar = Calendar.getInstance()
        selectedDateTimeMillis?.let {
            calendar.timeInMillis = it
        }

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

    private fun updateEvent() {
        val oldEvent = currentEvent ?: return
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

        val updatedEvent = oldEvent.copy(
            title = title,
            category = category,
            location = location,
            dateTimeMillis = dateMillis
        )

        viewModel.update(updatedEvent)
        Toast.makeText(requireContext(), "Event updated successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun deleteEvent() {
        val event = currentEvent ?: return
        viewModel.delete(event)
        Toast.makeText(requireContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}