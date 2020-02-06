package com.othman.tripbuddies.utils

import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel

object FirebaseUserHelper {


    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

}
