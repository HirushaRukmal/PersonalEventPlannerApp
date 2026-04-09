package com.example.personalplanner.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalplanner.R
import com.example.personalplanner.data.EventViewModel
import com.example.personalplanner.databinding.FragmentEventListBinding

class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()
    private lateinit var adapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEventListBinding.bind(view)

        adapter = EventAdapter(emptyList()) { event ->
            val bundle = Bundle().apply {
                putInt("eventId", event.id)
            }
            findNavController().navigate(
                R.id.action_eventListFragment_to_editEventFragment,
                bundle
            )
        }

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = adapter

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.updateData(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}