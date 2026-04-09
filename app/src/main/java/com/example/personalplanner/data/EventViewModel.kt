package com.example.personalplanner.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.personalplanner.EventApplication
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository =
        (application as EventApplication).repository

    val allEvents: LiveData<List<Event>> = repository.allEvents

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch {
        repository.update(event)
    }

    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }

    suspend fun getEventById(id: Int): Event? {
        return repository.getEventById(id)
    }
}