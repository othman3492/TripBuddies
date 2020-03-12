package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.internal.Mutable
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import com.othman.tripbuddies.repositories.FirestoreUserRepository
import java.util.concurrent.Executor

class FirestoreUserViewModel : ViewModel() {


    private var userRepository = FirestoreUserRepository()
    var user: MutableLiveData<User> = MutableLiveData()
    private var usersList: MutableLiveData<List<User>> = MutableLiveData()


    // CREATE
    fun createUserIntoFirestore(user: User) = userRepository.createUser(user)


    // UPDATE
    fun updateUserIntoFirestore(user: User) = userRepository.updateUser(user)
    fun addTripToUser(userId: String, tripId: String) = userRepository.addTripToUser(userId, tripId)
    fun addCityToWishList(userId: String, cityId: String) = userRepository.addCityToWishList(userId, cityId)
    fun addVisitedCity(userId: String, cityId: String) = userRepository.addVisitedCity(userId, cityId)
    fun removeTripFromUser(userId: String, tripId: String) = userRepository.removeTripFromUser(userId, tripId)
    fun removeCityFromWishList(userId: String, cityId: String) = userRepository.removeCityFromWishList(userId, cityId)
    fun removeVisitedCity(userId: String, cityId: String) = userRepository.removeVisitedCity(userId, cityId)


    // DELETE
    fun deleteUserFromFirestore(userId: String) = userRepository.deleteUser(userId)



    // Retrieve single user from Firestore and convert it to usable LiveData
    fun getUser(userId: String): LiveData<User> {

        userRepository.getUser(userId).addSnapshotListener { doc, e ->
            if (e != null) {
                this.user.value = null
                return@addSnapshotListener
            }

            val savedUser = doc!!.toObject(User::class.java)

            this.user.value = savedUser
        }

        return this.user
    }


    // Retrieve user list from Firestore and convert it to usable List<LiveData>
    fun getAllUsers(): LiveData<List<User>> {

        userRepository.getAllUsers().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                usersList.value = null
                return@EventListener
            }

            val savedUsersList: MutableList<User> = mutableListOf()
            for (doc in value!!) {

                val user = doc.toObject(User::class.java)
                savedUsersList.add(user)
            }

            usersList.value = savedUsersList
        })

        return usersList
    }

}