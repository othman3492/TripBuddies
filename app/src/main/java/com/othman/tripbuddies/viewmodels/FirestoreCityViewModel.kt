package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.internal.Mutable
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreCityRepository
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import java.util.concurrent.Executor

class FirestoreCityViewModel: ViewModel() {


    var cityRepository = FirestoreCityRepository()
    var city: MutableLiveData<City> = MutableLiveData()
    var cityList: MutableLiveData<List<City>> = MutableLiveData()


    // CREATE
    fun createCityIntoFirestore(city: City) = cityRepository.createCity(city)


    // UPDATE
    fun updateCityIntoFirestore(city: City) = cityRepository.updateCity(city)
    fun addUserToWishList(cityId: String, userId: String) = cityRepository.addUserToWishList(cityId, userId)
    fun addTripToCity(cityId: String, tripId: String) = cityRepository.addTripToCity(cityId, tripId)
    fun addMessageToChat(cityId: String, messageId: String) = cityRepository.addMessageToChat(cityId, messageId)
    fun removeUserFromWishList(cityId: String, userId: String) = cityRepository.removeUserFromWishList(cityId, userId)
    fun removeTripFromCity(cityId: String, tripId: String) = cityRepository.removeTripFromCity(cityId, tripId)
    fun removeMessageFromChat(cityId: String, messageId: String) = cityRepository.removeMessageFromChat(cityId, messageId)


    // DELETE
    fun deleteCity(cityId: String) = cityRepository.deleteCity(cityId)



    // Retrieve single city from Firestore and convert it to usable LiveData
    fun getCity(cityId: String): LiveData<City> {

        cityRepository.getCity(cityId).addSnapshotListener { doc, e ->
            if (e != null) {
                this.city.value = null
                return@addSnapshotListener
            }

            val savedCity = doc!!.toObject(City::class.java)

            this.city.value = savedCity
        }

        return this.city
    }


    // Retrieve city list from Firestore and convert it to usable List<LiveData>
    fun getAllCities(): LiveData<List<City>> {

        cityRepository.getAllCities().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityList.value = null
                return@EventListener
            }

            val savedCityList: MutableList<City> = mutableListOf()
            for (doc in value!!) {

                val city = doc.toObject(City::class.java)
                savedCityList.add(city)
            }

            cityList.value = savedCityList
        })

        return cityList
    }







}