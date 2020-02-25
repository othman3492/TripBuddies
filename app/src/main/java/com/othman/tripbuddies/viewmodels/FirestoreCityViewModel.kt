package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreCityRepository
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import java.util.concurrent.Executor

class FirestoreCityViewModel: ViewModel() {


    var cityRepository = FirestoreCityRepository()
    var city: MutableLiveData<City> = MutableLiveData()
    var userWishList: MutableLiveData<List<City>> = MutableLiveData()
    var tripCityList: MutableLiveData<List<City>> = MutableLiveData()


    // CREATE
    fun addCityToTrip(trip: Trip, city: City) = cityRepository.addCityToTrip(trip, city)


    // DELETE
    fun removeCityFromTrip(trip: Trip, city: City) = cityRepository.removeCityFromTrip(trip, city)





    // Retrieve trip's city list from Firestore and convert it to usable List<LiveData>
    fun getAllCitiesFromTrip(trip: Trip): LiveData<List<City>> {

        cityRepository.getAllCitiesFromUser(trip.userId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripCityList.value = null
                return@EventListener
            }

            val savedTripCityList: MutableList<City> = mutableListOf()
            for (doc in value!!) {

                val city = doc.toObject(City::class.java)
                savedTripCityList.add(city)
            }

            tripCityList.value = savedTripCityList
        })

        return tripCityList
    }



}