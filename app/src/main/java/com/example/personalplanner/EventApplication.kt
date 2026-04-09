package com.example.personalplanner

import android.app.Application
import com.example.personalplanner.data.EventDatabase
import com.example.personalplanner.data.EventRepository

class EventApplication : Application() {
    val database by lazy { EventDatabase.getDatabase(this) }
    val repository by lazy { EventRepository(database.eventDao()) }
}