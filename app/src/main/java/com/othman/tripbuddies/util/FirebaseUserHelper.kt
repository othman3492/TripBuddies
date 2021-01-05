package com.othman.tripbuddies.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseUserHelper {


    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

}
