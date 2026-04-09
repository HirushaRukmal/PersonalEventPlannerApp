package com.example.personalplanner.data

class EventRepository(private val eventDao: EventDao) {

    val allEvents = eventDao.getAllEvents()

    suspend fun insert(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun update(event: Event) {
        eventDao.updateEvent(event)
    }

    suspend fun delete(event: Event) {
        eventDao.deleteEvent(event)
    }

    suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)
    }
}