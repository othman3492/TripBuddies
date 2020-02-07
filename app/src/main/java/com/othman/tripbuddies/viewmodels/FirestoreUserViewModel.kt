package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import com.othman.tripbuddies.repositories.FirestoreUserRepository
import java.util.concurrent.Executor

class FirestoreUserViewModel : ViewModel() {


    private var userRepository = FirestoreUserRepository()
    var user: MutableLiveData<User> = MutableLiveData()
    private var userList: MutableLiveData<List<User>> = MutableLiveData()


    // CREATE
    fun createUserIntoFirestore(user: User) = userRepository.createUser(user)


    // UPDATE
    fun updateUserIntoFirestore(user: User) = userRepository.updateUser(user)


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

                userList.value = null
                return@EventListener
            }

            val savedUserList: MutableList<User> = mutableListOf()
            for (doc in value!!) {

                val user = doc.toObject(User::class.java)
                savedUserList.add(user)
            }

            userList.value = savedUserList
        })

        return userList
    }


}