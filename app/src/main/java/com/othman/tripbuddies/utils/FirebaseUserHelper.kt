package com.othman.tripbuddies.utils

import com.google.firebase.auth.FirebaseAuth

object FirebaseUserHelper {


    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

}
