package com.othman.tripbuddies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.othman.tripbuddies.data.database.FirestoreDatabase
import com.othman.tripbuddies.data.model.City
import kotlinx.coroutines.launch

class FirestoreCityViewModel : ViewModel() {


    private val database = FirestoreDatabase()

    private val _city = MutableLiveData<City>()
    private val city: LiveData<City> = _city
    private val _cityList = MutableLiveData<List<City>>()
    private val cityList: LiveData<List<City>> = _cityList


    // Retrieve single city from Firestore and convert it to usable LiveData
    fun getCity(cityId: String): LiveData<City> {

        viewModelScope.launch {
            _city.value = database.getCity(cityId)
        }
        return city
    }


    // Retrieve city list from Firestore and convert it to usable List<LiveData>
    fun getAllCities(): LiveData<List<City>> {

        viewModelScope.launch {
            _cityList.value = database.getAllCities()
        }
        return cityList
    }


    // CREATE
    fun createCityIntoFirestore(city: City) = database.createCity(city)


    // UPDATE
    fun updateCityIntoFirestore(city: City) = database.updateCity(city)
    fun addUserToWishList(cityId: String, userId: String) =
        database.addUserToWishList(cityId, userId)

    fun addTripToCity(cityId: String, tripId: String) = database.addTripToCity(cityId, tripId)

    fun addMessageToChat(cityId: String, messageId: String) =
        database.addMessageToChat(cityId, messageId)

    fun removeUserFromWishList(cityId: String, userId: String) =
        database.removeUserFromWishList(cityId, userId)

    fun removeTripFromCity(cityId: String, tripId: String) =
        database.removeTripFromCity(cityId, tripId)

    fun removeMessageFromChat(cityId: String, messageId: String) =
        database.removeMessageFromChat(cityId, messageId)


    // DELETE
    fun deleteCity(cityId: String) = database.deleteCity(cityId)


}