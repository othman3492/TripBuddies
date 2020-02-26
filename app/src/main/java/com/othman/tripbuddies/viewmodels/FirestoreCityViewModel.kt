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
    var cityVisitorsList: MutableLiveData<List<User>> = MutableLiveData()
    var cityWishList: MutableLiveData<List<User>> = MutableLiveData()
    var cityTripList: MutableLiveData<List<Trip>> = MutableLiveData()
    var chatMessageList: MutableLiveData<List<Message>> = MutableLiveData()


    // CREATE
    fun createCityIntoFirestore(city: City) = cityRepository.createCity(city)
    fun addVisitorToCity(cityId: String, user: User) = cityRepository.addVisitorToCity(cityId, user)
    fun addUserToWishList(cityId: String, user: User) = cityRepository.addUserToWishList(cityId, user)
    fun addTripToCity(cityId: String, trip: Trip) = cityRepository.addTripToCity(cityId, trip)
    fun createMessage(cityId: String, message: Message) = cityRepository.createMessage(cityId, message)


    // UPDATE
    fun updateCityIntoFirestore(city: City) = cityRepository.updateCity(city)



    // DELETE
    fun deleteCity(cityId: String) = cityRepository.deleteCity(cityId)
    fun removeVisitorFromCity(cityId: String, user: User) = cityRepository.removeVisitorFromCity(cityId, user)
    fun removeUserFromWishList(cityId: String, user: User) = cityRepository.removeUserFromWishList(cityId, user)
    fun removeTripFromCity(cityId: String, trip: Trip) = cityRepository.removeTripFromCity(cityId, trip)
    fun deleteMessage(cityId: String, message: Message) = cityRepository.deleteMessage(cityId, message)



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
    fun getAllVisitorsFromCity(cityId: String): LiveData<List<User>> {

        cityRepository.getAllVisitorsFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityVisitorsList.value = null
                return@EventListener
            }

            val savedCityVisitorsList: MutableList<User> = mutableListOf()
            for (doc in value!!) {

                val user = doc.toObject(User::class.java)
                savedCityVisitorsList.add(user)
            }

            cityVisitorsList.value = savedCityVisitorsList
        })

        return cityVisitorsList
    }


    // Retrieve city's wish list from Firestore and convert it to usable List<LiveData>
    fun getWishListFromCity(cityId: String): LiveData<List<User>> {

        cityRepository.getWishListFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityWishList.value = null
                return@EventListener
            }

            val savedCityWishList: MutableList<User> = mutableListOf()
            for (doc in value!!) {

                val user = doc.toObject(User::class.java)
                savedCityWishList.add(user)
            }

            cityWishList.value = savedCityWishList
        })

        return cityWishList
    }


    // Retrieve city's trip list from Firestore and convert it to usable List<LiveData>
    fun getAllTripsFromCity(cityId: String): LiveData<List<Trip>> {

        cityRepository.getAllTripsFromCity(cityId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityTripList.value = null
                return@EventListener
            }

            val savedCityTripList: MutableList<Trip> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.toObject(Trip::class.java)
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