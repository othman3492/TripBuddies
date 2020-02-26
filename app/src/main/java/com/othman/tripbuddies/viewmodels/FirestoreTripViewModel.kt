package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreTripRepository

class FirestoreTripViewModel: ViewModel() {


    var tripRepository = FirestoreTripRepository()
    var trip: MutableLiveData<Trip> = MutableLiveData()
    var tripList: MutableLiveData<List<Trip>> = MutableLiveData()
    var tripBuddiesList: MutableLiveData<List<User>> = MutableLiveData()
    var tripCitiesList: MutableLiveData<List<City>> = MutableLiveData()
    var tripImagesList: MutableLiveData<List<String>> = MutableLiveData()


    // CREATE
    fun createTripIntoFirestore(trip: Trip) = tripRepository.createTrip(trip)
    fun addBuddyToTrip(tripId: String, user: User) = tripRepository.addBuddyToTrip(tripId, user)
    fun addCityToTrip(tripId: String, city: City) = tripRepository.addCityToTrip(tripId, city)
    fun addPhotoToTrip(tripId: String, path: String) = tripRepository.addPhotoToTrip(tripId, path)


    // UPDATE
    fun updateTripIntoFirestore(trip: Trip) = tripRepository.updateTrip(trip)


    // DELETE
    fun deleteTripFromFirestore(trip: Trip) = tripRepository.deleteTrip(trip)
    fun removeBuddyFromTrip(tripId: String, user: User) = tripRepository.removeBuddyFromTrip(tripId, user)
    fun removeCityFromTrip(tripId: String, city: City) = tripRepository.removeCityFromTrip(tripId, city)
    fun removePhotoFromTrip(tripId: String, path: String) = tripRepository.removePhotoFromTrip(tripId, path)



    // Retrieve single trip from Firestore and convert it to usable LiveData
    fun getTrip(tripId: String): LiveData<Trip> {

        tripRepository.getTrip(tripId).addSnapshotListener { doc, e ->
            if (e != null) {
                this.trip.value = null
                return@addSnapshotListener
            }

            val savedTrip = doc!!.toObject(Trip::class.java)

            this.trip.value = savedTrip
        }

        return this.trip
    }


    // Retrieve trip list from Firestore and convert it to usable List<LiveData>
    fun getAllTrips(): LiveData<List<Trip>> {

        tripRepository.getAllTrips().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripList.value = null
                return@EventListener
            }

            val savedTripList: MutableList<Trip> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.toObject(Trip::class.java)
                savedTripList.add(trip)
            }

            tripList.value = savedTripList
        })

        return tripList
    }



    // Retrieve trip's buddies list from Firestore and convert it to usable List<LiveData>
    fun getAllBuddiesFromTrip(tripId: String): LiveData<List<User>> {

        tripRepository.getAllBuddiesFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripBuddiesList.value = null
                return@EventListener
            }

            val savedTripBuddiesList: MutableList<User> = mutableListOf()
            for (doc in value!!) {

                val user = doc.toObject(User::class.java)
                savedTripBuddiesList.add(user)
            }

            tripBuddiesList.value = savedTripBuddiesList
        })

        return tripBuddiesList
    }


    // Retrieve trip's destinations list from Firestore and convert it to usable List<LiveData>
    fun getAllCitiesFromTrip(tripId: String): LiveData<List<City>> {

        tripRepository.getAllCitiesFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripCitiesList.value = null
                return@EventListener
            }

            val savedTripCitiesList: MutableList<City> = mutableListOf()
            for (doc in value!!) {

                val city = doc.toObject(City::class.java)
                savedTripCitiesList.add(city)
            }

            tripCitiesList.value = savedTripCitiesList
        })

        return tripCitiesList
    }



    // Retrieve trip's photo list from Firestore and convert it to usable List<LiveData>
    fun getPhotosFromTrip(tripId: String): LiveData<List<String>> {

        tripRepository.getPhotosFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripImagesList.value = null
                return@EventListener
            }

            val savedTripImagesList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val photo = doc.toObject(String::class.java)
                savedTripImagesList.add(photo)
            }

            tripImagesList.value = savedTripImagesList
        })

        return tripImagesList
    }



}