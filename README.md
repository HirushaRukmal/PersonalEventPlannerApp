# Personal Event Planner App

## Overview
The Personal Event Planner App is an Android application developed in Kotlin using Android Studio. It helps users manage personal events, trips, and appointments through a simple and structured interface.

The app supports full CRUD functionality:
- Create new events
- Read and view upcoming events
- Update existing events
- Delete events

The app uses the Room Persistence Library to store data locally on the device, so event data remains available even after the app is closed or the device is restarted.

---

## Features

### 1. Create Event
Users can add a new event with:
- Title
- Category
- Location
- Date and Time

### 2. View Events
The dashboard displays all upcoming events sorted by date.

### 3. Update Event
Users can select an existing event and modify its details.

### 4. Delete Event
Users can remove events they no longer wish to keep.

### 5. Local Data Persistence
The app uses Room Database to save event data locally.

### 6. Navigation
The app uses:
- Jetpack Navigation Component
- Bottom Navigation Bar
- Fragments instead of multiple Activities

### 7. Validation and Error Handling
The app includes:
- Title field validation
- Date selection validation
- Prevention of past dates
- Toast messages for success and error feedback

---

## Technologies Used
- Kotlin
- Android Studio
- Room Database
- RecyclerView
- Jetpack Navigation Component
- Fragments
- View Binding
- Material Design Components

---

## Project Structure

```text
app/
 └── src/main/
      ├── java/com/example/personalplanner/
      │    ├── MainActivity.kt
      │    ├── EventApplication.kt
      │    ├── data/
      │    │    ├── Event.kt
      │    │    ├── EventDao.kt
      │    │    ├── EventDatabase.kt
      │    │    ├── EventRepository.kt
      │    │    └── EventViewModel.kt
      │    └── ui/
      │         ├── EventListFragment.kt
      │         ├── AddEventFragment.kt
      │         ├── EditEventFragment.kt
      │         └── EventAdapter.kt
      ├── res/
      │    ├── layout/
      │    ├── menu/
      │    ├── navigation/
      │    ├── values/
      │    └── xml/
      └── AndroidManifest.xml
