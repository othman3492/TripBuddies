package com.othman.tripbuddies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.data.database.FirestoreDatabase
import com.othman.tripbuddies.data.model.User
import kotlinx.coroutines.launch

class FirestoreUserViewModel : ViewModel() {


    private val database = FirestoreDatabase()

    private val _user = MutableLiveData<User>()
    private val user: LiveData<User> = _user
    private val _userList = MutableLiveData<List<User>>()
    private val userList: LiveData<List<User>> = _userList


    // Retrieve single user from Firestore and convert it to usable LiveData
    fun getUser(userId: String): LiveData<User> {

        viewModelScope.launch {
            _user.value = database.getUser(userId)
        }
        return user
    }


    // Retrieve user list from Firestore and convert it to usable List<LiveData>
    fun getAllUsers(): LiveData<List<User>> {

        viewModelScope.launch {
            _userList.value = database.getAllUsers()
        }
        return userList
    }


    // CREATE
    fun createUserIntoFirestore(user: User) = database.createUser(user)


    // UPDATE
    fun updateUserIntoFirestore(user: User) = database.updateUser(user)
    fun addTripToUser(userId: String, tripId: String) = database.addTripToUser(userId, tripId)
    fun addCityToWishList(userId: String, cityId: String) = database.addCityToWishList(userId, cityId)
    fun removeTripFromUser(userId: String, tripId: String) = database.removeTripFromUser(userId, tripId)
    fun removeCityFromWishList(userId: String, cityId: String) = database.removeCityFromWishList(userId, cityId)


    // DELETE
    fun deleteUserFromFirestore(userId: String) = database.deleteUser(userId)

}