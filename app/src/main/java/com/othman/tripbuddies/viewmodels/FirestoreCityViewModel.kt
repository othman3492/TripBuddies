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
    var cityVisitorsList: MutableLiveData<List<String>> = MutableLiveData()
    var cityWishList: MutableLiveData<List<String>> = MutableLiveData()
    var cityTripList: MutableLiveData<List<String>> = MutableLiveData()
    var chatMessageList: MutableLiveData<List<Message>> = MutableLiveData()


    // CREATE
    fun createCityIntoFirestore(city: City) = cityRepository.createCity(city)
    fun addVisitorToCity(cityId: String, userId: String) = cityRepository.addVisitorToCity(cityId, userId)
    fun addUserToWishList(cityId: String, userId: String) = cityRepository.addUserToWishList(cityId, userId)
    fun addTripToCity(cityId: String, tripId: String) = cityRepository.addTripToCity(cityId, tripId)
    fun createMessage(cityId: String, message: Message) = cityRepository.createMessage(cityId, message)


    // UPDATE
    fun updateCityIntoFirestore(city: City) = cityRepository.updateCity(city)



    // DELETE
    fun deleteCity(cityId: String) = cityRepository.deleteCity(cityId)
    fun removeVisitorFromCity(cityId: String, userId: String) = cityRepository.removeVisitorFromCity(cityId, userId)
    fun removeUserFromWishList(cityId: String, userId: String) = cityRepository.removeUserFromWishList(cityId, userId)
    fun removeTripFromCity(cityId: String, tripId: String) = cityRepository.removeTripFromCity(cityId, tripId)
    fun deleteMessage(cityId: String, messageId: String) = cityRepository.deleteMessage(cityId, messageId)



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


    // Retrieve city's visitors list from Firestore and convert it to usable List<LiveData>
    fun getAllVisitorsFromCity(cityId: String): LiveData<List<String>> {

        cityRepository.getAllVisitorsFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityVisitorsList.value = null
                return@EventListener
            }

            val savedCityVisitorsList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val user = doc.id
                savedCityVisitorsList.add(user)
            }

            cityVisitorsList.value = savedCityVisitorsList
        })

        return cityVisitorsList
    }


    // Retrieve city's wish list from Firestore and convert it to usable List<LiveData>
    fun getWishListFromCity(cityId: String): LiveData<List<String>> {

        cityRepository.getWishListFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityWishList.value = null
                return@EventListener
            }

            val savedCityWishList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val user = doc.id
                savedCityWishList.add(user)
            }

            cityWishList.value = savedCityWishList
        })

        return cityWishList
    }


    // Retrieve city's trip list from Firestore and convert it to usable List<LiveData>
    fun getAllTripsFromCity(cityId: String): LiveData<List<String>> {

        cityRepository.getAllTripsFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityTripList.value = null
                return@EventListener
            }

            val savedCityTripList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.id
                savedCityTripList.add(trip)
            }

            cityTripList.value = savedCityTripList
        })

        return cityTripList
    }


    // Retrieve city's chat message list from Firestore and convert it to usable List<LiveData>
    fun getAllMessagesFromChat(cityId: String): LiveData<List<Message>> {

        cityRepository.getAllMessagesFromChat(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                chatMessageList.value = null
                return@EventListener
            }

            val savedChatMessageList: MutableList<Message> = mutableListOf()
            for (doc in value!!) {

                val message = doc.toObject(Message::class.java)
                savedChatMessageList.add(message)
            }

            chatMessageList.value = savedChatMessageList
        })

        return chatMessageList
    }







}