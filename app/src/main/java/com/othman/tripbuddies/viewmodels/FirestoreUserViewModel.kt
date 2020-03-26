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


    // CREATE
    fun createUserIntoFirestore(user: User) = userRepository.createUser(user)


    // UPDATE
    fun updateUserIntoFirestore(user: User) = userRepository.updateUser(user)
    fun addTripToUser(userId: String, tripId: String) = userRepository.addTripToUser(userId, tripId)
    fun addCityToWishList(userId: String, cityId: String) = userRepository.addCityToWishList(userId, cityId)
    fun removeTripFromUser(userId: String, tripId: String) = userRepository.removeTripFromUser(userId, tripId)
    fun removeCityFromWishList(userId: String, cityId: String) = userRepository.removeCityFromWishList(userId, cityId)


    // DELETE
    fun deleteUserFromFirestore(userId: String) = userRepository.deleteUser(userId)



    // Retrieve single user from Firestore and convert it to usable LiveData
    fun getUser(userId: String): LiveData<User> {

        val user: MutableLiveData<User> = MutableLiveData()
        userRepository.getUser(userId).addSnapshotListener { doc, e ->
            if (e != null) {
                user.value = null
                return@addSnapshotListener
            }

            val savedUser = doc!!.toObject(User::class.java)

            user.value = savedUser
        }

        return user
    }


    // Retrieve user list from Firestore and convert it to usable List<LiveData>
    fun getAllUsers(): LiveData<List<User>> {

        val usersList: MutableLiveData<List<User>> = MutableLiveData()
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