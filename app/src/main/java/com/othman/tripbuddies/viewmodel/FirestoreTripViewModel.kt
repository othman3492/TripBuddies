package com.othman.tripbuddies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.data.database.FirestoreDatabase
import com.othman.tripbuddies.data.model.City
import com.othman.tripbuddies.data.model.Trip
import kotlinx.coroutines.launch

class FirestoreTripViewModel: ViewModel() {


    private val database = FirestoreDatabase()

    private val _trip = MutableLiveData<Trip>()
    private val trip: LiveData<Trip> = _trip
    private val _tripList = MutableLiveData<List<Trip>>()
    private val tripList: LiveData<List<Trip>> = _tripList


    // Retrieve single trip from Firestore and convert it to usable LiveData
    fun getTrip(tripId: String): LiveData<Trip> {

        viewModelScope.launch {
            _trip.value = database.getTrip(tripId)
        }
        return trip
    }


    // Retrieve trip list from Firestore and convert it to usable List<LiveData>
    fun getAllTrips(): LiveData<List<Trip>> {

        viewModelScope.launch {
            _tripList.value = database.getAllTrips()
        }
        return tripList
    }


    // CREATE
    fun createTripIntoFirestore(trip: Trip) = database.createTrip(trip)


    // UPDATE
    fun updateTripIntoFirestore(trip: Trip) = database.updateTrip(trip)
    fun addBuddyToTrip(tripId: String, userId: String) = database.addBuddyToTrip(tripId, userId)
    fun addCityToTrip(tripId: String, cityId: String) = database.addCityToTrip(tripId, cityId)
    fun addPhotoToTrip(tripId: String, photo: String) = database.addPhotoToTrip(tripId, photo)
    fun removeBuddyFromTrip(tripId: String, userId: String) = database.removeBuddyFromTrip(tripId, userId)
    fun removeCityFromTrip(tripId: String, cityId: String) = database.removeCityFromTrip(tripId, cityId)
    fun removePhotoFromTrip(tripId: String, photo: String) = database.removePhotoFromTrip(tripId, photo)


    // DELETE
    fun deleteTripFromFirestore(tripId: String) = database.deleteTrip(tripId)

}